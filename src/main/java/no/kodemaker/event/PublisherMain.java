package no.kodemaker.event;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import static akka.actor.Actors.actorOf;

public class PublisherMain {
    public static void main(String[] args) {
        ActorRef publisher = actorOf(new UntypedActorFactory() {
            public UntypedActor create() {
                return new Publisher("tcp://*:5555", new StringFormat());
            }
        }).start();

        int count = 0;
        while (true) {
            publisher.sendOneWay("Message " + count++);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}