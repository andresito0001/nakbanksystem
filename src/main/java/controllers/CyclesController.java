package main.java.controllers;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import main.java.dao.CicleDAO;
import main.java.dao.InventoryDAO;
import main.java.entities.Clients;
import main.java.entities.Cycle;
import main.java.util.DatabaseUtils;
import main.java.util.SceneSwitcher;

public class CyclesController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
        configComponents();
        cycleTableView.setPlaceholder(new Label("No hay ciclos para este filtro"));

    }

    public void configComponents() {
        statusCombobox.getItems().addAll("ALL", "OK", "RECIBIDO");
        clientCiBar.setText(null);
    }

    public void updateTable() throws SQLException { 

        if (fromDatepicker.getValue() != null && toDatePicker.getValue() != null) {
            String condicion = "";

            if (clientCiBar.getText() != null && !clientCiBar.getText().isEmpty()) {
                condicion += " and cedula_cliente = '" + clientCiBar.getText().toString() + "'";
            }
    
            if (!statusCombobox.getSelectionModel().isEmpty()) {
                condicion += " and status = '" + statusCombobox.getSelectionModel().getSelectedItem().toString() + "'";
            }


            CicleDAO cicleDAO = new CicleDAO();

            ObservableList listaCycles = FXCollections.observableArrayList(); 
            
            cicleDAO.fillByDate(listaCycles, Date.valueOf(fromDatepicker.getValue()), Date.valueOf(toDatePicker.getValue()), condicion);
            
            if (listaCycles.isEmpty() == true) {
                System.out.println("No hay ciclos");
            }
            else {
                cycleTableView.setItems(listaCycles);

                timeTablecolumn.setCellValueFactory(new PropertyValueFactory<Cycle, String>("date"));
                clientTablecolumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClient().getAlias()));
                reciveTablecolumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getQuantityReceived().toString() + " " + param.getValue().getCurrencyReceived()));
                methodTablecolumn.setCellValueFactory(new PropertyValueFactory<>("receivedMethod"));
                sendTablecolumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSentQuantity().toString() + " " + param.getValue().getSentCurrency()));
                sendMethodTablecolumn.setCellValueFactory(new PropertyValueFactory<>("sentMethod"));
                rate.setCellValueFactory(new PropertyValueFactory<>("rate"));
                status.setCellValueFactory(new PropertyValueFactory<>("status"));
    
            }


            
            


    
        } else {
            Alert alert = new Alert(AlertType.WARNING, "Debe seleccionar una fecha de inicio y fin");
            alert.showAndWait();
        }
        





    }

    public void newCycle () throws SQLException {
        DatabaseUtils databaseUtils = new DatabaseUtils();
        if (databaseUtils.getInfoByLastReferenceOf("ciclos", "status", null, null).equals("ACTIVE")) {
            Alert alert = new Alert(AlertType.WARNING, "Ya existe un ciclo activo. Debe cerrar para crear uno nuevo.");
            alert.showAndWait();
        }
        else {
            try {
                SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/newCycle.fxml", "/main/resources/css/newCycle.css", new newCycleController());
            } catch (Exception e) {
                e.printStackTrace();
            }   
        }

    }

    public void closeCycle () throws SQLException {

        DatabaseUtils databaseUtils = new DatabaseUtils();
        //Obtengo la info del ultimo ciclo
        String lastCycleId = databaseUtils.getInfoByLastReferenceOf("ciclos", "id", null, null);
        CicleDAO cicleDAO = new CicleDAO();
        Cycle lastCycle = cicleDAO.getCycle(lastCycleId);

        if (lastCycle.getStatus().equals("ACTIVE")) {

            Double spendOfCycle = cicleDAO.getAvailableByCycle(lastCycleId);
            Double availableBalanceOfCycle = ( lastCycle.getQuantityReceived() - spendOfCycle ) / lastCycle.getRate();
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Del ciclo # " + lastCycle.getId() + " quedan " + availableBalanceOfCycle + " USD. " + "¿Desea cerrar el ciclo?");
            alert.showAndWait();
            if(alert.getResult().equals(ButtonType.OK)) {
                //Calculo disponible del ciclo: (cantidad_recibida - cantidad_enviada) / tasa madre
                if (availableBalanceOfCycle <= 10.00) {
                    databaseUtils.updateRegister("ciclos", "status", "INACTIVE", "id = '" + lastCycleId + "'"); 
                    Alert alerta = new Alert(AlertType.INFORMATION, "Se ha cerrado el ciclo #" + lastCycleId);
                    alerta.showAndWait();
                }
                else {
                    Alert alerta = new Alert(AlertType.ERROR, "Del ciclo #" + lastCycleId + " quedan " + availableBalanceOfCycle + " USD. No se recomienda cerrar el ciclo. ");
                    alerta.showAndWait();
                }
               
            }
        } else {
        Alert alert = new Alert(AlertType.WARNING, "No hay ningun ciclo activo para cerrar.");
        alert.showAndWait();
     }
    }

    @FXML
    private Button newCycleButtomn;
    @FXML
    private ComboBox<String> statusCombobox;
    @FXML
    private TextField clientCiBar;
    @FXML
    private DatePicker fromDatepicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private TableView<Cycle> cycleTableView;
    @FXML
    private TableColumn<Cycle, String> timeTablecolumn;
    @FXML
    private TableColumn<Cycle, String> clientTablecolumn;
    @FXML
    private TableColumn<Cycle, String> reciveTablecolumn;
    @FXML
    private TableColumn<Cycle, String> methodTablecolumn;
    @FXML
    private TableColumn<Cycle, String> sendTablecolumn;
    @FXML
    private TableColumn<Cycle, String> sendMethodTablecolumn;
    @FXML
    private TableColumn<Cycle, String> rate;
    @FXML
    private TableColumn<Cycle, String> status;


    @FXML
    private BorderPane borderPane;
}
