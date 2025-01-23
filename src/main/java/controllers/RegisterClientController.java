package main.java.controllers;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.java.dao.ClientsDAO;
import main.java.entities.Clients;
import main.java.util.ConnectionPool;

public class RegisterClientController {
    @FXML
    private ComboBox<String> filterTypeId;
    @FXML
    private Label registerMessageId;
    @FXML
    private TextField cedulaId;
    @FXML
    private TextField nombreId;
    @FXML
    private TextField apellidoId;
    @FXML
    private TextField aliasId;

    private String documentType;

    public void initialize() {
        filterTypeId.getItems().addAll("V-", "E-", "J-");
        filterTypeId.setOnAction(event -> {
        
        documentType = filterTypeId.getSelectionModel().getSelectedItem().toString();
        });
    }
    @FXML
    public void registerClient(MouseEvent event) throws SQLException {
        if (cedulaId.getText().isEmpty() == true || documentType == null) {
            registerMessageId.setId("errorMessageId");
            registerMessageId.setText("Error. Debe ingresar la cedula del cliente para su registro. ");
        }
        else {
                registerMessageId.setId("registerMessageId");
                ClientsDAO clientsDAO = new ClientsDAO(ConnectionPool.getConnection());
                Clients cliente = new Clients(documentType + cedulaId.getText().toString(), nombreId.getText().toString(), apellidoId.getText().toString(), aliasId.getText().toString());
                clientsDAO.insertClient(cliente);
                registerMessageId.setText("Cliente registrado exitosamente. ");
        }

    }

    
}
