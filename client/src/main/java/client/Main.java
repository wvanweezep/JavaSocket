package client;

import client.scenes.MainCtrl;
import client.scenes.ConnectionCtrl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private final MainCtrl mainCtrl = new MainCtrl();

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/connect.fxml"));
        ConnectionCtrl connectionCtrl = new ConnectionCtrl(mainCtrl);
        loader.setController(connectionCtrl);
        mainCtrl.initialize(stage, new Scene(loader.load()), connectionCtrl);
    }
}
