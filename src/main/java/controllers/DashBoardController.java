package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.java.Main;
import main.java.util.SceneSwitcher;

public class DashBoardController {
    @FXML
    private void loadTransactions(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(dashBoardPane, vBoxCenterPane, "/main/resources/fxml/transactionsOptions.fxml", "/main/resources/css/transactionOptions.css", new TransactionsController());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Load Transactions...");
    }

    @FXML
    private void loadDashboard(MouseEvent event) {
        Main.switchToDashboard();
    }

    @FXML
    private void loadClients(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(dashBoardPane, vBoxCenterPane, "/main/resources/fxml/clientsOptions.fxml", "/main/resources/css/clientOptions.css", new ClientsController());
        } catch (Exception e ) {
            e.printStackTrace();
        }
        System.out.println("Load clients...");
    }

    // . . .
    @FXML
    private BorderPane dashBoardPane;
    @FXML
    private VBox vBoxCenterPane;

}
