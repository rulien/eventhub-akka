package no.kodemaker.event.durable;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import no.kodemaker.event.StringFormat;

import static akka.actor.Actors.actorOf;
import static no.kodemaker.event.Subscriber.Receive;

public class SyncingSubscriberMain {

    public static void main(String[] args) {
        final ActorRef logger = actorOf(Logger.class).start();
        ActorRef subscriber = actorOf(new UntypedActorFactory() {
            public UntypedActor create() {
                return new SyncingSubscriber("tcp://localhost:5555", new StringFormat(), logger, "tcp://localhost:5554");
            }
        }).start();
        subscriber.sendOneWay(Receive);
    }

    public static class Logger extends UntypedActor {
        @Override
        public void onReceive(Object message) throws Exception {
            log().logger().info("Received " + message);
        }
    }
}
