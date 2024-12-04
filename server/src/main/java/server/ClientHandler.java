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

    private void userSetUp() throws IOException {
        String msg;
        String username = null;
        String password = null;
        while ((msg = in.readLine()) != null) {
            if (username == null) { username = msg; }
            else if (password == null) {
                password = msg;
                user = new User(username, password);
                sendObject(new Message<User>(user));
                break;
            }
        }
    }

    public <T> void sendObject(Message<T> obj) {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() throws IOException {
        while (in.available() != 0) {
            try{
                Object obj = (Object) in.readObject();
                System.out.println("Received object: " + obj);
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
