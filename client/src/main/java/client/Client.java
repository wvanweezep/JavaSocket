package client;

import commons.Message;
import commons.entities.User;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private User user = new User("marjooy", "OSUSUCKS");

    public void start(String host, int port) throws IOException {
        socket = new Socket(host, port);
        log("Connected to the server");
        out = new ObjectOutputStream(socket.getOutputStream());
        in =  new ObjectInputStream(socket.getInputStream());

        new Thread(this::listenForMessages).start();

        sendMessage(user);

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String msg;
        while ((msg = userInput.readLine()) != null) {
            sendMessage(msg);
        }
    }

    private void listenForMessages() {
        while (true) {
            try {
                Message<?> obj = (Message<?>) in.readObject();
                System.out.println("[" + obj.getSender().getUsername() + "] "
                        + (obj.getObject().isPresent() ? obj.getObject().get() : null));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> void sendMessage(T obj) {
        try {
            out.writeObject(new Message<T>(user, obj));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <S> void log(S msg) {
        System.out.println("[" + user.getUsername() + "] " + msg.toString());
    }
}
