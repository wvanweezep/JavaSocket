package server.scenes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import server.ClientHandler;

import java.util.Timer;
import java.util.TimerTask;

public class OverviewCtrl {

    private final MainCtrl mainCtrl;

    @FXML //Label for showing the Server IP
    private Label ipLabel;
    @FXML //Label for showing the Server Port
    private Label portLabel;
    @FXML //List of connected clients
    private ListView<ClientHandler> clientList;
    @FXML //List of server logs
    private ListView<String> serverLog;
    @FXML //TextArea for collapsed server log
    private TextArea serverLogField;


    public OverviewCtrl(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    /**
     * Called upon scene initialization
     */
    @FXML
    public void initialize() {
        TimerTask saveTask = new TimerTask() {
            @Override
            public void run() { updateCall(); }};
        new Timer().scheduleAtFixedRate(saveTask, 0, 100);
    }

    /**
     * Sets the information fields (IP and Port) in the Overview
     */
    private void initServerInfo(){
        if (mainCtrl.getServer().getIp() == null || mainCtrl.getServer().getPort() == null) return;
        ipLabel.setText("IP: " + mainCtrl.getServer().getIp());
        portLabel.setText("Port: " + mainCtrl.getServer().getPort().toString());
    }

    /**
     * Method call for updating the overview without interrupting JavaFX thread.
     */
    private void updateCall() {
        Platform.runLater(this::updateOverview);
    }

    /**
     * Update the Overview lists (Connected Clients, Open Rooms, Server Logs).
     */
    private void updateOverview() {
        if (mainCtrl.getServer() == null) return;
        initServerInfo();
        if (!clientList.getItems().equals(mainCtrl.getServer().getClients()))
            clientList.getItems().setAll(mainCtrl.getServer().getClients());
        if (!serverLog.getItems().equals(mainCtrl.getServer().getDebugger().getLog()))
            serverLog.getItems().setAll(mainCtrl.getServer().getDebugger().getLog());
    }

    /**
     * Set the TextArea to the selected server log message.
     */
    public void collapseLog() {
        if (serverLog.getSelectionModel().getSelectedItem() != null) {
            serverLogField.setText(serverLog.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Disconnect a client when kicked.
     */
    public void kickClient() {
        if (clientList.getSelectionModel().getSelectedItem() == null) return;
        mainCtrl.getServer().disconnectClient(
                clientList.getSelectionModel().getSelectedItem());
    }
}
