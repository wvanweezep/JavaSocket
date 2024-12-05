package server.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;
import server.Server;

import java.io.IOException;

public class MainCtrl {

    private Server server;
    private Stage primaryStage;

    private Scene overview;
    private OverviewCtrl overviewCtrl;

    public MainCtrl() {}

    /**
     * Setup and start of JavaFX application and {@code Server}.
     *
     * @param primaryStage The {@code Stage} of the running application
     * @param overview The {@code Scene} of the {@code OverviewCtrl}
     * @param overviewCtrl The instantiated {@code OverviewCtrl}
     * @throws IOException Thrown on an input/output error
     */
    public void initialize(Stage primaryStage, Scene overview, OverviewCtrl overviewCtrl) throws IOException {
        this.primaryStage = primaryStage;
        this.overview = overview;
        this.overviewCtrl = overviewCtrl;

        initServer();
        showOverview();
        primaryStage.show();
    }

    //Getters and Setters
    public Server getServer() {
        return this.server;
    }


    /**
     * Starting protocol of the {@code Server}.
     */
    private void initServer() {
        this.server = new Server();
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

    /**
     * Activate the Overview scene
     */
    public void showOverview() {
        primaryStage.setTitle("Server Overview");
        primaryStage.setScene(overview);
    }
}