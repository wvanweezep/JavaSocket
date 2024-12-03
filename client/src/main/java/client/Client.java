package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(String host, int port) throws IOException {
        socket = new Socket(host, port);
        log("Connected to the server");
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(this::listenForMessages).start();

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String msg;
        while ((msg = userInput.readLine()) != null) {
            sendMessage(msg);
        }
    }

    private void listenForMessages() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                System.out.println("Server: " + msg);
            }
        } catch (IOException e) {
            System.err.println("Error reading from server: " + e.getMessage());
        }
    }

    private void sendMessage(String message) {
        out.println(message);
    }

    private static <S> void log(S msg) {
        System.out.println("[Client] " + msg.toString());
    }
}
