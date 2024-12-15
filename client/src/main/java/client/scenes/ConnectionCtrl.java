package client.scenes;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConnectionCtrl {

    private final MainCtrl mainCtrl;
    private String status;
    private final Object lock = new Object();

    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private Label connectionStatusLabel;

    public ConnectionCtrl(MainCtrl mainCtrl) { this.mainCtrl = mainCtrl; }

    public void attemptConnection() {
        status = null;
        try {
            Client client = initClient(ipField.getText(), Integer.parseInt(portField.getText()));
            waitForStatus();
            handleStatus();
        } catch (Exception e) {
            connectionStatusLabel.setText("Failed to connect: " + e.getMessage());
        }
    }

    public void setStatus(String status) {
        synchronized (lock) {
            this.status = status; // Or any status value
            lock.notify();
        }
    }

    private void waitForStatus() {
        synchronized (lock) {
            while (status == null) {
                try{ lock.wait(); }
                catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private boolean handleStatus() {
        if (status.equals("Connected to the server"))
            System.out.println(status);
        else connectionStatusLabel.setText(status);
        System.out.println(status);
        return true;
    }

    private Client initClient(String host, int port) {
        Client client = new Client();
        Thread clientThread = new Thread(() -> {
            try {
                client.start(host, port, this);
            } catch (IOException e) {
                setStatus("Failed to connect: " + e.getMessage());
            }
        });
        clientThread.setDaemon(true);
        clientThread.start();
        return client;
    }
}
