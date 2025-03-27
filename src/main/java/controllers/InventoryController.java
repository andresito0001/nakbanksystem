package main.java.controllers;

import java.sql.Date;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.dao.InventoryDAO;
import main.java.entities.Inventory;

public class InventoryController {


    public void initialize () {

        MoneyType.getItems().addAll("ALL", "VES", "USD", "USDT", "EUR", "BTC");
        MovType.getItems().addAll("ALL", "INGRESO", "EGRESO");
        Type.getItems().addAll("ALL", "COMPRA", "SWAP", "CICLO", "ABONO", "INVERSION", "TRASPASO", "GASTO");

        inventoryTableView.setPlaceholder(new Label("No hay registros en inventario para este ciclo"));
    }
    
    public void updateTable () throws SQLException {

        if (fromDatepicker.getValue() != null && toDatePicker.getValue() != null) {
            String condicion = "";

            if (!MoneyType.getSelectionModel().isEmpty() && !MoneyType.getSelectionModel().getSelectedItem().equals("ALL")) {
                condicion += " and moneda = '" + MoneyType.getSelectionModel().getSelectedItem().toString() + "'";
            }
            if (!MovType.getSelectionModel().isEmpty() && !MovType.getSelectionModel().getSelectedItem().equals("ALL")) {
                condicion += " and tipo_movimiento = '" + MovType.getSelectionModel().getSelectedItem().toString() + "'";
            }
            if (!Type.getSelectionModel().isEmpty() && !Type.getSelectionModel().getSelectedItem().equals("ALL")) {
                condicion += " and tipo = '" + Type.getSelectionModel().getSelectedItem().toString() + "'";
            }

            InventoryDAO inventoryDAO = new InventoryDAO();

            ObservableList listaInventory = FXCollections.observableArrayList(); 
            
            inventoryDAO.fillByDate(listaInventory, Date.valueOf(fromDatepicker.getValue()), Date.valueOf(toDatePicker.getValue()), condicion);
            
            inventoryTableView.setItems(listaInventory);

                timeTablecolumn.setCellValueFactory(new PropertyValueFactory<Inventory, String>("date"));
                MovTableColumn.setCellValueFactory(new PropertyValueFactory<Inventory, String>("movementType"));
                TypeTablecolumn.setCellValueFactory(new PropertyValueFactory<Inventory, String>("operationType"));
                QuantityTablecolumn.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("quantity"));
                CurrencyTablecolumn.setCellValueFactory(new PropertyValueFactory<Inventory, String>("moneyType"));
                MethodTablecolumn.setCellValueFactory(new PropertyValueFactory<Inventory, String>("method"));
    
            if (listaInventory.isEmpty() == true) {
                    inventoryTableView.setPlaceholder(new Label("No se encontraron registros en inventario para estos filtros"));
            }
            
        } else {
            Alert alert = new Alert(AlertType.WARNING, "Debe seleccionar una fecha de inicio y fin");
            alert.showAndWait();
        }
        
    }

    @FXML
    private ComboBox<String> MoneyType;
    @FXML
    private ComboBox<String> MovType;
    @FXML
    private ComboBox<String> Type;
    @FXML
    private DatePicker fromDatepicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Button apllyFillButtonm;
    @FXML
    private TableView<Inventory> inventoryTableView;
    @FXML
    private TableColumn<Inventory, String> timeTablecolumn;
    @FXML
    private TableColumn<Inventory, String> MovTableColumn;
    @FXML
    private TableColumn<Inventory, String> TypeTablecolumn;
    @FXML
    private TableColumn<Inventory, Double> QuantityTablecolumn;
    @FXML
    private TableColumn<Inventory, String> CurrencyTablecolumn;
    @FXML
    private TableColumn<Inventory, String> MethodTablecolumn;


}
