package main.java.controllers;

import java.sql.SQLException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.java.dao.ClientsDAO;
import main.java.entities.Clients;
import main.java.util.ConnectionPool;
import main.java.util.SceneSwitcher;

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
    @FXML
    private Pane anchorPaneId;

    private String documentType;

    public void initialize() {
        filterTypeId.getItems().addAll("V-", "E-", "J-");
        
        filterTypeId.setOnAction(event -> {
        documentType = filterTypeId.getSelectionModel().getSelectedItem().toString();
        cedulaId.setText(documentType);
        });

        formatosTextField();
     }

    public void formatosTextField () {
        
        UnaryOperator <TextFormatter.Change> filterCedula = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("V-\\d{0,8}")) {
                return change;
            }
            else if (newText.matches("J-\\d{0,10}")) {
                return change;
            }
            else if (newText.matches("E-\\d{0,8}")) {
                return change;
            }
            return null;
        };

        UnaryOperator <TextFormatter.Change> filterText = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> formatoCedula = new TextFormatter<>(filterCedula);
        TextFormatter<String> formatoNombre = new TextFormatter<>(filterText);
        TextFormatter<String> formatoApellido = new TextFormatter<>(filterText);
        TextFormatter<String> formatoAlias = new TextFormatter<>(filterText);

        cedulaId.setTextFormatter(formatoCedula);
        nombreId.setTextFormatter(formatoNombre);
        apellidoId.setTextFormatter(formatoApellido);
        aliasId.setTextFormatter(formatoAlias);

        cedulaId.positionCaret(cedulaId.getText().length());
    }

    public void back () {
        try {
            SceneSwitcher.switchPane(anchorPaneId, "/main/resources/fxml/clientsOptions.fxml", "/main/resources/css/clientsOptions.css", new ClientsOptionsController());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                try {
                    Clients cliente = new Clients(cedulaId.getText().toString(), nombreId.getText().toString(), apellidoId.getText().toString(), aliasId.getText().toString());
                    clientsDAO.insertClient(cliente);
                    registerMessageId.setText("Cliente registrado exitosamente. ");
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR, "Error. Este cliente ya existe");
                    alert.showAndWait();
                }
        }
    }
}
