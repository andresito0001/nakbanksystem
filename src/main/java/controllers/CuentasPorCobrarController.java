package main.java.controllers;

import java.sql.SQLException;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.java.entities.CxC;
import main.java.util.ConnectionPool;
import main.java.util.SceneSwitcher;

public class CuentasPorCobrarController {
    @FXML
    private TableColumn<CxC, String> trxId;
    @FXML
    private TableColumn<CxC, String> clientId;
    @FXML
    private TableColumn<CxC, String> montoId;
    @FXML
    private TableColumn<CxC, Double> abonadoId;
    @FXML
    private TableColumn<CxC, String> pendienteId;
    @FXML
    private TableView<CxC> cxcTableId;
    @FXML
    private TextField transactionIdAbono;
    @FXML
    private Label abonoLabelId;
    @FXML
    private Button abonoButtonId;

    private ObservableList<CxC> listaCxC;
    @FXML
    public void initialize() throws SQLException {
        cxcTableId.setPlaceholder(new Label("No hay cuentas por cobrar pendientes"));
        listaCxC = FXCollections.observableArrayList();
    
        CxC.generarLista(ConnectionPool.getConnection(), listaCxC);
        cxcTableId.setItems(listaCxC);

        trxId.setCellValueFactory(new PropertyValueFactory<CxC, String>("idTransaction"));
        clientId.setCellValueFactory(new PropertyValueFactory<CxC, String>("cliente"));
        montoId.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMontoTransaccion().toString() + " " + param.getValue().getMonedaTransaccion()));
        //montoId.setCellValueFactory(new PropertyValueFactory<CxC, Double>("montoTransaccion"));
        abonadoId.setCellValueFactory(new PropertyValueFactory<CxC, Double>("abonadoTransaccion"));
        pendienteId.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPendienteTransaccion().toString() + " " + param.getValue().getMonedaTransaccion()));

        if (listaCxC.isEmpty() == true) {
            transactionIdAbono.setVisible(false);
            abonoLabelId.setVisible(false);
            abonoButtonId.setVisible(false);
        }

        
    }
    @FXML
    public void mostrarContenido(MouseEvent event) {

        CxC cuenta = cxcTableId.getSelectionModel().getSelectedItem();

        if (cuenta == null) {
            System.out.println("No seleccionó una cuenta");
        }
        else {
            String headerMensaje = "¿Desea abonar a esta cuenta?";
            String infoCuenta = "Transaccion #" + cuenta.getIdTransaction() + ". Cliente: " + cuenta.getCliente() + ". Total Transaccion: " + cuenta.getMontoTransaccion() + ". Total Abonado: " + cuenta.getAbonadoTransaccion() + ". Total Pendiente: " + cuenta.getPendienteTransaccion();
            
            Alert alert = new Alert (AlertType.CONFIRMATION,infoCuenta);
            alert.setHeaderText(headerMensaje);
            alert.setTitle("Abono de Cuenta");
            alert.showAndWait();

            if(alert.getResult() == ButtonType.OK) {
                System.out.println("loading 'modulo de abono'");
                try {
                    AbonoCuentaController.setCuenta(cuenta);
                    SceneSwitcher.switchPane(anchorPane, "/main/resources/fxml/abonoCuenta.fxml", "/main/resources/css/abonoCuenta.css", new AbonoCuentaController());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(alert.getResult() == ButtonType.CANCEL) {
                cxcTableId.getSelectionModel().clearSelection();
                System.out.println("Abono cancelado");
            }
        }      
    }

    @FXML
    private Pane anchorPane;
    
}
