package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.java.util.SceneSwitcher;

public class ClientsOptionsController {
    @FXML
    public void newClient(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/registerUser.fxml", "/main/resources/css/registerClients.css", new RegisterClientController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findClient(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/findClient.fxml", "/main/resources/css/findClient.css", new FindClientController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Pane borderPane;
}
