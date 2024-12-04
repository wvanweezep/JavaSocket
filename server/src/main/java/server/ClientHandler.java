package server;

import commons.Message;
import commons.User;

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
            System.err.println("Connection error: " + e.getMessage());
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
                sendMessage("Received user");
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> void sendMessage(T obj) {
        try {
            out.writeObject(new Message<T>(obj));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() throws IOException {
        while (true) {
            try{
                Message<?> obj = (Message<?>) in.readObject();
                System.out.println("Received object: " + (obj.getObject().isPresent() ? obj.getObject().get() : null));
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
}
