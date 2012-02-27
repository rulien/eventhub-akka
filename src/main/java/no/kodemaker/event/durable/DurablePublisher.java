package no.kodemaker.event.durable;

import akka.actor.UntypedActor;
import no.kodemaker.event.Writer;
import org.zeromq.ZMQ;

public class DurablePublisher extends UntypedActor {
	private final String endpoint;
    private final String syncEndpoint;
	private final Writer<Object> writer;
	private ZMQ.Context context;
	private ZMQ.Socket publisher;
    private ZMQ.Socket sync;

	protected DurablePublisher(String endpoint, Writer<Object> writer, String syncEndpoint) {
		this.endpoint = endpoint;
		this.writer = writer;
        this.syncEndpoint = syncEndpoint;
    }

	@Override
	public void preStart() {
		context = ZMQ.context(5);
		publisher = context.socket(ZMQ.PUB);
        sync = context.socket(ZMQ.PULL);
        sync.bind(syncEndpoint);
        publisher.bind(endpoint);
        sync.recv(0);
	}

	@Override
	public void postStop() {
        sync.close();
		publisher.close();
		context.term();
	}

	@Override
	public void onReceive(Object message) throws Exception {
        publisher.send(writer.write(message), 0);
	}
}
