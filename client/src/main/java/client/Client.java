package client;

import commons.Message;
import commons.entities.User;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private User user;

    public void start(String host, int port) throws IOException {
        socket = new Socket(host, port);
        log("Connected to the server");
        out = new ObjectOutputStream(socket.getOutputStream());
        in =  new ObjectInputStream(socket.getInputStream());

        login();
        new Thread(this::listenForMessages).start();

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String msg;
        while ((msg = userInput.readLine()) != null) {
            sendMessage(msg);
        }
    }

    @SuppressWarnings("unchecked")
    private void login() throws IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        boolean validated = false;
        while (!validated) {
            System.out.println("Enter username: ");
            String username = userInput.readLine();
            System.out.println("Enter password: ");
            String password = userInput.readLine();
            user = new User(username, password);
            sendMessage(user);

            try {
                Message<String> obj = (Message<String>) in.readObject();
                boolean found = false;
                while (!found){
                    if (obj.getSender().getUsername().equals("Server")) {
                        if (obj.getObject().isPresent()) {
                            validated = obj.getObject().get().equals(
                                    "Successfully logged in");
                            System.out.println("[" + obj.getSender().getUsername() + "] "
                                    + (obj.getObject().isPresent() ? obj.getObject().get() : null)
                                    + "\n");
                            found = true;
                        }
                    } else obj = (Message<String>) in.readObject();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
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
        System.out.println("[" + (user != null ? user.getUsername() : "client") + "] " + msg.toString());
    }
}
