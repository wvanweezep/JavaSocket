package server.utils;

import commons.entities.User;
import server.ClientHandler;
import server.Server;

import java.util.List;

public class ServerUtils {

    private final Server server;

    public ServerUtils(Server server) { this.server = server; }

    /**
     * Broadcasts a message to all clients.
     *
     * @param obj The content of the message
     * @param sender The {@code ClientHandler} accompanied to the sender
     * @param <T> The type for the message, any object is allowed
     */
    public <T> void broadcastToAll(T obj, ClientHandler sender) {
        server.getClients().forEach(c -> c.sendMessage(obj, sender.getUser()));
    }

    /**
     * Broadcasts a message to all clients, except the sender.
     *
     * @param obj The content of the message
     * @param sender The {@code ClientHandler} accompanied to the sender
     * @param <T> The type for the message, any object is allowed
     */
    public <T> void broadcast(T obj, ClientHandler sender) {
        server.getClients().forEach(c -> {
            if (!c.equals(sender)) c.sendMessage(obj, sender.getUser());
        });
    }

    /**
     * Broadcasts a message to a selected group of clients, excluding the sender.
     *
     * @param obj The content of the message
     * @param sender The {@code ClientHandler} accompanied to the sender
     * @param receivers The {@code ClientHandler} of the receivers
     * @param <T> The type for the message, any object is allowed
     */
    public <T> void broadcast(T obj, ClientHandler sender, List<ClientHandler> receivers) {
        server.getClients().stream()
                .filter(c -> !c.equals(sender) && receivers.contains(c))
                .forEach(c -> c.sendMessage(obj, sender.getUser()));
    }

    /**
     * Checks if a {@code User} is already connected to the {@code Server}.
     *
     * @param client The {@code ClientHandler} to exclude from the search
     * @param user The {@code User} to check for
     * @return {@code true} if the {@code User} is already connected
     */
    public boolean isConnected(ClientHandler client, User user) {
        return server.getClients().stream()     //
                .filter(c -> !c.equals(client)) //
                .map(ClientHandler::getUser)    //
                .anyMatch(user::equals);        //
    }
}
