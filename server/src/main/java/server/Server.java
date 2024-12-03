package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        log("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            log("New client connected " + clientSocket.getInetAddress());
            ClientHandler clientHandler = new ClientHandler(clientSocket, this);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    public void broadcast(String msg) {
        for (ClientHandler client : clients) {
                client.sendMessage(msg);
        }
    }

    public void broadcast(String msg, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(msg);
            }
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    private static <S> void log(S msg) {
        System.out.println("[Server] " + msg.toString());
    }
}
