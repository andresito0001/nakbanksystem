package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.java.util.SceneSwitcher;

public class ContabilidadOptionsController {
    @FXML
    private void loadCxC(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/cuentasPorCobrar.fxml", "/main/resources/css/registerClients.css", new CuentasPorCobrarController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Pane borderPane;
}
