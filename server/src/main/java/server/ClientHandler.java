package server;

import commons.Message;
import commons.entities.User;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private User user;
    private long latency = 0;

    public ClientHandler(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            userSetUp();
            receiveMessage();
        } catch (IOException | ClassNotFoundException e) {
            server.getDebugger().log("[" + (user != null ? user.getUsername() : "unnamed")
                    + "] Disconnected");
        } finally { disconnect(); }
    }

    //Getters and Setters
    public User getUser() {
        return user;
    }

    public long getLatency() {
        return latency;
    }


    /**
     * Handles the login of the {@code Client}. A {@code Client} gets denied
     * if it's either already connected to the {@code Server} or the
     * username-password combination is incorrect.
     *
     * @throws IOException Thrown on an input/output error
     * @throws ClassNotFoundException Thrown if not recognizable object
     * can be read from the input
     */
    @SuppressWarnings("unchecked")
    private void userSetUp() throws IOException, ClassNotFoundException {
        while (true) {
            this.user = null;
            Message<User> obj = (Message<User>) in.readObject();
            this.user = obj.getObject().orElse(null);
            if (!server.getUsersDatabase().authenticateUser(this.user)) {
                sendMessage("Incorrect username/password combination", server.getIdentity());
                continue; }
            if (server.getUtils().isConnected(this, this.user)) {
                sendMessage("User already connected", server.getIdentity());
                continue; }
            sendMessage("Successfully logged in", server.getIdentity());
            return;
        }
    }

    /**
     * Sends a message to the {@code Client}.
     *
     * @param obj The obj being sent to the {@code Client}
     * @param sender The {@code User} who sent the message
     * @param <T> The type for the message, any object is allowed
     */
    public <T> void sendMessage(T obj, User sender) {
        try {
            out.writeObject(new Message<T>(sender, obj));
            out.flush();
        } catch (Exception e) {
            server.log("Failed to send message: " + sender.getUsername()
                    + " -> " + (user != null ? user.getUsername() : "unnamed"));
        }
    }

    /**
     * Receives messages from the {@code ObjectInputStream}.
     *
     * @throws IOException Thrown on an input/ output error
     */
    private void receiveMessage() throws IOException, ClassNotFoundException {
        while (true) {
            Message<?> obj = (Message<?>) in.readObject();
            server.getDebugger().log("[" + obj.getSender().getUsername() + "] "
                    + (obj.getObject().isPresent() ? obj.getObject().get() : null));
            server.getUtils().broadcast(obj.getObject().get(), this);
            latency = Duration.between(obj.getTimestamp(), Instant.now()).toMillis();
        }
    }

    /**
     * Handles closing a connection of a {@code Client}.
     */
    protected void disconnect() {
        try {
            socket.close();
        } catch(IOException e) {
            server.log("Error closing client ("
                    + (user != null ? user.getUsername() : "unnamed")
                    + ") connection: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        if (user == null) return "[Unregistered User]";
        return user.getUsername() + " (" + latency + "ms)";
    }
}
