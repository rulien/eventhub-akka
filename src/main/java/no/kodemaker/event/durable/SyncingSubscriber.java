package no.kodemaker.event.durable;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import no.kodemaker.event.Reader;
import org.zeromq.ZMQ;

public class SyncingSubscriber extends UntypedActor {
    public static final String Receive = "Receive";
    private final String endpoint;
    private final String syncEndpoint;
    private final Reader<Object> reader;
    private final ActorRef actor;
    private ZMQ.Context context;
    private ZMQ.Socket subscriber;
    private ZMQ.Socket sync;

    public SyncingSubscriber(String endpoint, Reader<Object> reader, ActorRef actor, String syncEndpoint) {
        this.endpoint = endpoint;
        this.reader = reader;
        this.actor = actor;
        this.syncEndpoint = syncEndpoint;
    }

    @Override
    public void preStart() {
        context = ZMQ.context(5);
        subscriber = context.socket(ZMQ.SUB);
        subscriber.setIdentity("MyBurableInbox".getBytes());
        subscriber.connect(endpoint);
        subscriber.subscribe("".getBytes());
        sync = context.socket(ZMQ.PUSH);
        sync.connect(syncEndpoint);
        sync.send("".getBytes(), 0);
    }

    @Override
    public void postStop() {
        subscriber.close();
        context.term();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (Receive.equals(message)) {
            byte[] bytes = subscriber.recv(ZMQ.PAIR);
            if (bytes != null)
                actor.sendOneWay(reader.read(bytes), getContext());
            else
                Thread.sleep(1);
            getContext().sendOneWay(Receive);
        }
    }
}
