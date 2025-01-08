package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    private BorderPane dashBoardPane;
    @FXML
    private VBox vBoxCenterPane;

}
