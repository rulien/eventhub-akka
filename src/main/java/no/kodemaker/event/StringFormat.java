package no.kodemaker.event;

public class StringFormat implements Writer<Object>, Reader<Object> {
    public byte[] write(Object message) {
        return message == null ? new byte[0] : message.toString().getBytes();
    }

    public Object read(byte[] bytes) {
        return bytes == null ? "": new String(bytes);
    }
}
