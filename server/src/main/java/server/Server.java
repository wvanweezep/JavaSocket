package server;

import commons.Debugger;
import commons.entities.User;
import server.database.UserDatabase;
import server.utils.ServerUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final User identity = new User("Server", "d93hslFs");
    private final Debugger debugger = new Debugger();
    private final ServerUtils utils = new ServerUtils(this);
    private UserDatabase users;

    /**
     * Starting protocol of the {@code Server}, opening up for any clients to connect.
     *
     * @param port The port on which the {@code Server} is located
     * @throws IOException if any errors occur connecting a client or creating the {@code }
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        users = new UserDatabase(debugger);
        users.save(identity);
        log("Server started: (port = " + serverSocket.getLocalPort() +
                ", ip = " + InetAddress.getLocalHost().getHostAddress() + ")");
        while (true) { connectClient(serverSocket.accept()); }
    }

    //Getters and Setters
    public Integer getPort() {
        return serverSocket.getLocalPort();
    }

    public String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public Debugger getDebugger() {
        return debugger;
    }

    public ServerUtils getUtils() {
        return utils;
    }

    public User getIdentity() {
        return identity;
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public UserDatabase getUsersDatabase() {
        return users;
    }


    /**
     * Attempts to remove a {@code ClientHandler} from the list of clients.
     *
     * @param client The targeted {@code ClientHandler} to be removed
     */
    public void disconnectClient(ClientHandler client) {
        clients.remove(client);
        client.disconnect();
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
     * Documents an event in the {@code Debugger}.
     *
     * @param msg The message that describes the event
     * @param <T> The type for the message, any object is allowed
     */
    public <T> void log(T msg) {
        System.out.println("[Server] " + msg);
        debugger.log("[Server] " + msg);
    }
}
