package no.kodemaker.event.durable;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import no.kodemaker.event.StringFormat;

import static akka.actor.Actors.actorOf;

public class DurablePublisherMain {

    public static void main(String[] args) {
            ActorRef publisher = actorOf(new UntypedActorFactory() {
                public UntypedActor create() {
                    return new DurablePublisher("tcp://*:5555", new StringFormat(), "tcp://*:5554");
                }
            }).start();

            int count = 0;
            while (true) {
                int i = count++;
                publisher.sendOneWay("Message " + i);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
}
