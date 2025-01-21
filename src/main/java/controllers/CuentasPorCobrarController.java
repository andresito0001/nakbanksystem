package main.java.controllers;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.entities.CxC;
import main.java.util.ConnectionPool;

public class CuentasPorCobrarController {
    @FXML
    private TableColumn<CxC, String> trxId;
    @FXML
    private TableColumn<CxC, String> clientId;
    @FXML
    private TableColumn<CxC, Double> montoId;
    @FXML
    private TableColumn<CxC, Double> abonadoId;
    @FXML
    private TableColumn<CxC, Double> pendienteId;
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
        cxcTableId.getSelectionModel().setCellSelectionEnabled(true);
        cxcTableId.setPlaceholder(new Label("No hay cuentas por cobrar pendientes"));
        listaCxC = FXCollections.observableArrayList();
    
        CxC.generarLista(ConnectionPool.getConnection(), listaCxC);
        cxcTableId.setItems(listaCxC);

        trxId.setCellValueFactory(new PropertyValueFactory<CxC, String>("idTransaction"));
        clientId.setCellValueFactory(new PropertyValueFactory<CxC, String>("cliente"));
        montoId.setCellValueFactory(new PropertyValueFactory<CxC, Double>("montoTransaccion"));
        abonadoId.setCellValueFactory(new PropertyValueFactory<CxC, Double>("abonadoTransaccion"));
        pendienteId.setCellValueFactory(new PropertyValueFactory<CxC, Double>("pendienteTransaccion"));

        if (listaCxC.isEmpty() == true) {
            transactionIdAbono.setVisible(false);
            abonoLabelId.setVisible(false);
            abonoButtonId.setVisible(false);
        }
    }




    
    
}
