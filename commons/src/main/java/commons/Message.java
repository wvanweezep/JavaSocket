package commons;

import commons.entities.User;

import java.io.*;
import java.util.Optional;

public class Message<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final User sender;
    private final byte[] serializedData;

    public Message(User sender, T obj) {
        this.sender = sender;
        this.serializedData = serializeObject(obj);
    }

    public User getSender() {
        return sender;
    }

    public Optional<T> getObject() {
        return deserializeObject(serializedData);
    }

    private byte[] serializeObject(T object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException e) {
            return new byte[]{};
        }
    }

    @SuppressWarnings("unchecked")
    private Optional<T> deserializeObject(byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return Optional.ofNullable((T) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "serializedDataSize=" + (serializedData != null ? serializedData.length : 0) +
                '}';
    }
}
