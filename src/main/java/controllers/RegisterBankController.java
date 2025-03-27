package main.java.controllers;

import java.sql.SQLException;
import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import main.java.dao.BanksDAO;
import main.java.entities.Banks;

public class RegisterBankController {

    @FXML
    private ComboBox filterTypeId;
    @FXML
    private TextField nombreId;
    @FXML
    private TextField codigoId;
    @FXML
    private TextField numeroCuentaId;
    @FXML
    private TextField correoId;

    public void initialize () {
        filterTypeId.getItems().addAll("VES", "USD", "USDT", "EUR", "BTC");
        crearFormatos();

    }

    public void crearFormatos () {
        UnaryOperator <TextFormatter.Change> filterNombre = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*") && newText.length() <= 20) {
                return change;
            }
            return null;
        };
        UnaryOperator <TextFormatter.Change> filterCodigo = change -> {
            String newText = change.getControlNewText();

            if (newText.length() <= 10) {
                return change;
            }
            return null;
        };

        TextFormatter<String> formatoNombreBanco = new TextFormatter<>(filterNombre);
        TextFormatter<String> formatoCodigo = new TextFormatter<>(filterCodigo);

        nombreId.setTextFormatter(formatoNombreBanco);
        codigoId.setTextFormatter(formatoCodigo);
    }

    public void clearfields() {
        nombreId.clear();
        codigoId.clear();
        numeroCuentaId.clear();
        correoId.clear();
    }
    public void registerBank () throws SQLException {
        if (!codigoId.getText().isEmpty() &&  !nombreId.getText().isEmpty() && !correoId.getText().isEmpty() &&
            filterTypeId.getSelectionModel().getSelectedItem() != null) {
            
            Banks banks = new Banks(
                codigoId.getText().toString(), 
                nombreId.getText().toString(), 
                filterTypeId.getSelectionModel().getSelectedItem().toString(), 
                0.0, 
                correoId.getText().toString()
            );

            BanksDAO banksDAO = new BanksDAO();

            try {
                banksDAO.insertBank(banks);
                Alert alert = new Alert(AlertType.INFORMATION, "Banco registrado exitosamente");
                alert.showAndWait();

            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR, "Este banco ya existe. ");
                alert.showAndWait();
            }

            clearfields();

        } else {
            Alert alert = new Alert(AlertType.ERROR, "Ha ocurrido un error. Por favor, verifique todos los campos");
            alert.showAndWait();
        }


    }

}
