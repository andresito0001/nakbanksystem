package main.java.controllers;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class FindClientController {
    @FXML
    private TableColumn<Cliente,String> cedulaId; 

    private ObservableList<Cliente> listaClientes;

    @FXML
    public void initialize() throws SQLException {

    }

}
