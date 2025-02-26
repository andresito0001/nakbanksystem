package main.java.controllers;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.Main;
import main.java.dao.AdminDAO;
import main.java.util.SceneSwitcher;

public class ContabilidadOptionsController {
    @FXML
    private VBox inversionButtonId;
    @FXML
    private VBox ingresosButtonId;
    @FXML
    private VBox gastosButtonId;
    @FXML
    private VBox dividendosButton;

    public void initialize () throws SQLException {
        AdminDAO admin = new AdminDAO();
        String rol = admin.authenticateRol(Main.getUsername());

        if (!rol.equals("administrador")) {
            inversionButtonId.setVisible(false);
            ingresosButtonId.setVisible(false);
            gastosButtonId.setVisible(false);
            dividendosButton.setVisible(false);
        }

    }

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
