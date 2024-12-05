package server.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;
import server.Server;

import java.io.IOException;

public class MainCtrl {

    private Server server;
    private Stage primaryStage;
    private Scene overview;

    public MainCtrl() {}

    public void initialize(Stage primaryStage, Scene overview) throws IOException {
        this.primaryStage = primaryStage;
        this.overview = overview;

        initServer();
        showOverview();
        primaryStage.show();
    }

    private void initServer() {
        server = new Server();
        Thread serverThread = new Thread(() -> {
            try {
                server.start(8080);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    public Server getServer() {
        return server;
    }

    public void showOverview() {
        primaryStage.setTitle("Server Overview");
        primaryStage.setScene(overview);
    }
}