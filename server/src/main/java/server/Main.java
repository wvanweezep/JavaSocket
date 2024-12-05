package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.scenes.MainCtrl;
import server.scenes.OverviewCtrl;

import java.io.IOException;

public class Main extends Application {

    private final MainCtrl mainCtrl = new MainCtrl();

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/overview.fxml"));
        OverviewCtrl overviewCtrl = new OverviewCtrl(mainCtrl);
        loader.setController(overviewCtrl);
        mainCtrl.initialize(stage, new Scene(loader.load()), overviewCtrl);
    }
}
