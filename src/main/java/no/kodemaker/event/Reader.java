package no.kodemaker.event;

public interface Reader<T> {
    T read(byte[] bytes);
}
