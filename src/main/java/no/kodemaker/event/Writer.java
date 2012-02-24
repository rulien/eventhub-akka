package no.kodemaker.event;

public interface Writer<T> {
    byte[] write(T message);
}
