package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.java.util.SceneSwitcher;

public class ContabilidadOptionsController {
    @FXML
    private void loadCxC(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/cuentasPorCobrar.fxml", "/main/resources/css/cxc.css", new CuentasPorCobrarController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadCxP(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/cuentasPorPagar.fxml", "/main/resources/css/cxc.css", new CuentasPorPagarController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGastos(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/registerExpense.fxml", "/main/resources/css/registerExpense.css", new RegisterExpenseController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInversion(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/inversion.fxml", "/main/resources/css/inversion.css", new RegisterInversionController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Pane borderPane;
}
