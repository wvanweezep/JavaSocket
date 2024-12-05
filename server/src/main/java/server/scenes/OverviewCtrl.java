package server.scenes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import server.ClientHandler;

import java.util.Timer;
import java.util.TimerTask;

public class OverviewCtrl {

    private final MainCtrl mainCtrl;

    @FXML
    private Label ipLabel;
    @FXML
    private Label portLabel;
    @FXML
    private ListView<ClientHandler> clientList;
    @FXML
    private ListView<String> serverLog;


    public OverviewCtrl(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void initialize() {
        TimerTask saveTask = new TimerTask() {
            @Override
            public void run() { updateCall(); }
        };
        new Timer().scheduleAtFixedRate(saveTask, 0, 1000);
    }

    private void initServerInfo(){
        if (mainCtrl.getServer().getIp() == null || mainCtrl.getServer().getPort() == null) return;
        ipLabel.setText("IP: " + mainCtrl.getServer().getIp());
        portLabel.setText("Port: " + mainCtrl.getServer().getPort().toString());
    }

    private void updateCall() {
        Platform.runLater(this::updateOverview);
    }

    private void updateOverview() {
        if (mainCtrl.getServer() == null) return;
        initServerInfo();
        if (!clientList.getItems().equals(mainCtrl.getServer().getClients()))
            clientList.getItems().setAll(mainCtrl.getServer().getClients());
    }

    public void kickClient() {
        if (clientList.getSelectionModel().getSelectedItem() == null) return;
        mainCtrl.getServer().removeClient(
                clientList.getSelectionModel().getSelectedItem());
    }
}
