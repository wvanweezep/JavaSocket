package server;

import server.database.UserDatabase;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final UserDatabase users = new UserDatabase();

    /**
     * Starting protocol of the {@code Server}, opening up for any clients to connect.
     *
     * @param port The port on which the {@code Server} is located
     * @throws IOException if any errors occur connecting a client or creating the {@code }
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        log("Server started: (port = " + port +
                ", ip = " + InetAddress.getLocalHost().getHostAddress()
                + ")");

        while (true) {
            connectClient(serverSocket.accept());
        }
    }

    /**
     * Broadcasts a message to all clients.
     *
     * @param msg The content of the message
     */
    public void broadcastToAll(String msg) {
        clients.forEach(client -> client.sendMessage(msg));
    }

    /**
     * Broadcasts a message to all clients, except the sender.
     *
     * @param msg The content of the message
     * @param sender The {@code ClientHandler} accompanied to the sender
     */
    public void broadcast(String msg, ClientHandler sender) {
        clients.forEach(client -> { if (!client.equals(sender)) client.sendMessage(msg); });
    }

    /**
     * Broadcasts a message to a selected group of clients, excluding the sender.
     *
     * @param msg The content of the message
     * @param sender The {@code ClientHandler} accompanied to the sender
     * @param receivers The {@code ClientHandler} of the receivers
     */
    public void broadcast(String msg, ClientHandler sender, List<ClientHandler> receivers) {
        clients.stream()
                .filter(client -> !client.equals(sender) && receivers.contains(client))
                .forEach(client -> client.sendMessage(msg));
    }

    /**
     * Attempts to remove a {@code ClientHandler} from the list of clients.
     *
     * @param clientHandler The targeted {@code ClientHandler} to be removed
     * @return {@code true} if the {@code ClientHandler} was successfully removed
     */
    public boolean removeClient(ClientHandler clientHandler) {
        return clients.remove(clientHandler);
    }

    /**
     * Connects a new client to the {@code Server}.
     *
     * @param socket The clients {@code Socket}
     */
    public void connectClient(Socket socket) {
        log("New client connected " + socket.getInetAddress());
        ClientHandler clientHandler = new ClientHandler(socket, this);
        clients.add(clientHandler);
        new Thread(clientHandler).start();
    }

    /**
     * Documents an event in the terminal
     *
     * @param msg The message that describes the event
     * @param <S> The type for the message, any object is allowed
     */
    private static <S> void log(S msg) {
        System.out.println("[Server] " + msg.toString());
    }
}
