package main.java.controllers;


import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
