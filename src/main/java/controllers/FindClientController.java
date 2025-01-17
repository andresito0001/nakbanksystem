package main.java.controllers;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class FindClientController {
    /*@FXML
    private TableColumn<Cliente,String> cedulaId; 
    @FXML
    private ObservableList<Cliente> listaClientes;
    */
    @FXML
    private ComboBox<String> filterId;
    @FXML
    private Button findButtonId;
    @FXML
    private Label filterLabelId;
    @FXML
    public void initialize() throws SQLException {
        filterId.getItems().addAll("Cedula", "Nombre", "Apellido", "Alias");
        filterId.setOnAction(event -> {
            String data = filterId.getSelectionModel().getSelectedItem().toString();
            filterLabelId.setText("Buscar por: " + data);
        });
    }
    @FXML
    public void findClient(MouseEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION, "Modulo en Construccion!!!",ButtonType.CLOSE);
        alert.showAndWait();
    }
    /* 
    public void selectFilter(MouseEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION, filtroId.getItems().get(0),ButtonType.CLOSE);
        alert.showAndWait();
    }*/

}
