package main.java.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import main.java.dao.BanksDAO;
import main.java.entities.CxC;
import main.java.util.ConnectionPool;


public class AbonoCuentaController {
    @FXML
    private TextField montoId;
    @FXML
    private ComboBox<String> metodoId;
    @FXML
    private Label infoTransactionId;
    @FXML
    private Button abonarButton;
    @FXML
    private Button cancelarButton;
    @FXML
    private Label bankInfoId;
    @FXML
    private Label infoTransactionId1;
    @FXML
    private Label montoMaxId;

    private static CxC cuentaXAbonar;
    
    public void initialize () throws SQLException {
        List<String> bankCodes = new ArrayList<>();
        bankCodes = new BanksDAO(ConnectionPool.getConnection()).getInfoOf("codigo", null, null);
        metodoId.getItems().addAll(bankCodes); 
        montoMaxId.setText("Monto debe ser menor o igual a " + cuentaXAbonar.getPendienteTransaccion());
        infoTransactionId.setText("Cuenta #" + cuentaXAbonar.getIdTransaction() + ". Cliente: " + cuentaXAbonar.getCliente() + ". ");       
        infoTransactionId1.setText("Monto Total: " + cuentaXAbonar.getMontoTransaccion() + " " + cuentaXAbonar.getMonedaTransaccion() + ". Total Abonado: " + cuentaXAbonar.getAbonadoTransaccion() + ". Pendiente: " + cuentaXAbonar.getPendienteTransaccion());
    }

    public static void setCuenta(CxC cuentaXCobrar) {
        cuentaXAbonar = cuentaXCobrar;
    }

    public void abonarCuenta () {
        Double monto = Double.parseDouble(montoId.getText());
        if(monto <= cuentaXAbonar.getPendienteTransaccion())
        {
            
            Alert alert = new Alert(AlertType.INFORMATION, "Monto a abonar: " + monto.toString());
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(AlertType.INFORMATION, "Error en: " + monto.toString());
            alert.showAndWait();
        }

    }



}
