package client.scenes;

import client.Client;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainCtrl {

    private Client client;
    private Stage primaryStage;

    private Scene menu;
    private ConnectionCtrl connectionCtrl;

    public MainCtrl() {}

    public void initialize(Stage primaryStage, Scene menu, ConnectionCtrl connectionCtrl) {
        this.primaryStage = primaryStage;
        this.menu = menu;
        this.connectionCtrl = connectionCtrl;

        showConnect();
        primaryStage.show();
    }

    public Client getClient() { return this.client; }

    public void showConnect() {
        primaryStage.setTitle("Connect to Server");
        primaryStage.setScene(menu);
    }
}
