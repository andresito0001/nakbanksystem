package main.java.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import main.java.util.SceneSwitcher;

public class CyclesController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newCycleButtomn.setOnAction(_ -> {
            try {
                SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/newCycle.fxml", "/main/resources/css/newCycle.css", new newCycleController());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });   
    }

    @FXML
    private Button newCycleButtomn;

    @FXML
    private BorderPane borderPane;
}
