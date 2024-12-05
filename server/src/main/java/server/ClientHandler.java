package server;

import commons.Message;
import commons.entities.User;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private User user;

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
        } catch (IOException e) {
            log("Disconnected");
        } finally {
            close();
        }
    }

    @SuppressWarnings("unchecked")
    private void userSetUp() throws IOException {
        while (true) {
            try{
                Message<User> obj = (Message<User>) in.readObject();
                user = obj.getObject().orElse(null);
                if (server.getUsersDatabase().authenticateUser(user)) {
                    if (server.getClients().stream() //
                            .filter(c -> !c.equals(this))
                            .map(ClientHandler::getUser) //
                            .anyMatch(user::equals)) {
                        sendMessage("User already connected", server.getIdentity());
                    } else{
                        sendMessage("Successfully logged in", server.getIdentity());
                        return;
                    }
                } else{
                    sendMessage("Incorrect username/password combination", server.getIdentity());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public User getUser() {
        return user;
    }

    public <T> void sendMessage(T obj, User sender) {
        try {
            out.writeObject(new Message<T>(sender, obj));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() throws IOException {
        while (true) {
            try{
                Message<?> obj = (Message<?>) in.readObject();
                System.out.println("[" + obj.getSender().getUsername() + "] "
                        + (obj.getObject().isPresent() ? obj.getObject().get() : null));
                server.broadcast(obj.getObject().get(), this);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void close() {
        try {
            socket.close();
            if (!server.removeClient(this)) throw new IOException("Client not found");
        } catch(IOException e) {
            System.err.println("Error closing client connection: " + e.getMessage());
        }
    }

    private <S> S log(S msg) {
        System.out.println("[" + user.getUsername() + "] " + msg.toString());
        return msg;
    }

    @Override
    public String toString() {
        if (user == null) return "[Unregistered User]";
        return user.getUsername();
    }
}
