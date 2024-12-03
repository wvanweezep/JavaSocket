package server;

import commons.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;

    private PrintWriter out;
    private BufferedReader in;

    private User user;

    public ClientHandler(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
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
                sendMessage("Successfully created account");
                break;
            }
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    private void receiveMessage() throws IOException {
        String msg;
        while ((msg = in.readLine()) != null) {
            server.broadcast(log(msg), this);
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
