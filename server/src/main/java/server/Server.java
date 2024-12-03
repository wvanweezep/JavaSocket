package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private ServerSocket socket;

    public void start(int port) throws IOException {
        socket = new ServerSocket(port);
        log("Server started on port " + port);
    }

    private static void log(String msg) {
        System.out.println("[Server] " + msg);
    }
}
