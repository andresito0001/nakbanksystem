package main.java.controllers;


import java.sql.Connection;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import main.java.util.ConnectionPool;
import main.java.util.DatabaseUtils;
import main.java.util.SceneSwitcher;

public class TransactionsOptionsController {
    @FXML
    public void newTransaction(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/registerTransaction.fxml", "/main/resources/css/registerTransaction.css", new RegisterTransactionController());
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public void newCycle(MouseEvent event) {
        try {
            DatabaseUtils dbUtils = new DatabaseUtils(ConnectionPool.getConnection());
            if (dbUtils.getInfoByLastReferenceOf("cicles", "status", null, null).equals("ACTIVE")) {
                Alert alert = new Alert(AlertType.WARNING, "Ya existe un ciclo activo en este momento",ButtonType.CLOSE);
                alert.showAndWait();
                return;
            }

            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/newCycle.fxml", "/main/resources/css/newCycle.css", new newCycleController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consultTransaction(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/consultTransactions.fxml", "/main/resources/css/consultTransaction.css", new ConsultTransactionController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Pane borderPane;
}
