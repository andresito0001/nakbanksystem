package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

public class ConsultTransactionController {
    @FXML
    private Button findButton;
    @FXML
    private void findTrans(MouseEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION, "Modulo en Construccion!!!",ButtonType.CLOSE);
        alert.showAndWait();
    }
    
}
