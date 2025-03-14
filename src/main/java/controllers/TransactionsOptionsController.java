package main.java.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.java.dao.TransactionsDAO;
import main.java.entities.Clients;
import main.java.entities.TableviewTransaction;
import main.java.entities.TransactionFill;
import main.java.entities.Transactions;
import main.java.util.DatabaseUtils;
import main.java.util.SceneSwitcher;

public class TransactionsOptionsController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configControllers();

        apllyFillButtonm.setOnAction(e -> {
            try {
                updateTable();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

    }

    public void configControllers() {
        typeCombobox.getItems().addAll("ALL", "COMPRA", "SWAP");
        statusCombobox.getItems().addAll("ALL", "OK", "PENDIENTE", "RECIBIDO", "ENVIADO");
        clientCiBar.setText(null);
    }

    
    // public void configListeners() {
    //     clientCiBar.textProperty().addListener((observable, oldValue, newValue) -> {
    //         if (newValue.length() > 10) {
    //             clientCiBar.setText(newValue.substring(0, 8));
    //         }

    //         try {
    //             updateTable();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     });
    // }

    private void updateTable() throws SQLException {
        TransactionsDAO transactionsDAO = new TransactionsDAO();
        
        List<TableviewTransaction> transactionsIntableview = transactionsDAO.fillTransactionBy (
            new TransactionFill (
                clientCiBar.getText(),
                statusCombobox.getValue(),
                typeCombobox.getValue(),
                java.sql.Date.valueOf(fromDatepicker.getValue()),
                java.sql.Date.valueOf(toDatePicker.getValue())
            )
        );

        idTablecolumn.setCellValueFactory(new PropertyValueFactory<TableviewTransaction, String>("ID"));
        timeTablecolumn.setCellValueFactory(new PropertyValueFactory<TableviewTransaction, Time>("hora"));
        clientTablecolumn.setCellValueFactory(new PropertyValueFactory<TableviewTransaction, String>("clientCi"));
        reciveTablecolumn.setCellValueFactory(new PropertyValueFactory<TableviewTransaction, String>("qtyReciveWithCurrency"));
        methodTablecolumn.setCellValueFactory(new PropertyValueFactory<TableviewTransaction, String>("receivedMethod"));
        sendTablecolumn.setCellValueFactory(new PropertyValueFactory<TableviewTransaction, String>("qtySendWithCurrency"));
        sendMethodTablecolumn.setCellValueFactory(new PropertyValueFactory<TableviewTransaction, String>("sentMethod"));
        rate.setCellValueFactory(new PropertyValueFactory<TableviewTransaction, Double>("exchangeRate"));
        profit.setCellValueFactory(new PropertyValueFactory<TableviewTransaction, Double>("revenue"));

        transactionsTableview.getItems().setAll(transactionsIntableview);
    }

    @FXML
    public void newTransaction(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/registerTransaction.fxml", "/main/resources/css/registerTransaction.css", new RegisterTransactionController());
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    // public void newCycle(MouseEvent event) {
    //     try {
    //         DatabaseUtils dbUtils = new DatabaseUtils();
    //         if (dbUtils.getInfoByLastReferenceOf("ciclos", "status", null, null).equals("ACTIVE")) {
    //             Alert alert = new Alert(AlertType.WARNING, "Ya existe un ciclo activo en este momento",ButtonType.CLOSE);
    //             alert.showAndWait();
    //             return;
    //         }

    //         SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/newCycle.fxml", "/main/resources/css/newCycle.css", new newCycleController());
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // public void consultTransaction(MouseEvent event) {
    //     try {
    //         SceneSwitcher.switchPane(borderPane, "/main/resources/fxml/consultTransactions.fxml", "/main/resources/css/consultTransaction.css", new ConsultTransactionController());
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // fillter elements
    @FXML
    private TextField clientCiBar;
    @FXML
    private ComboBox<String> statusCombobox;
    @FXML
    private ComboBox<String> typeCombobox;
    @FXML
    private DatePicker fromDatepicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Button apllyFillButtonm;

    // table elements
    @FXML
    private TableView<TableviewTransaction> transactionsTableview;
    @FXML
    private TableColumn<TableviewTransaction, String> idTablecolumn;
    @FXML
    private TableColumn<TableviewTransaction, Time> timeTablecolumn;
    @FXML
    private TableColumn<TableviewTransaction, String> clientTablecolumn;
    @FXML
    private TableColumn<TableviewTransaction, String> reciveTablecolumn;
    @FXML
    private TableColumn<TableviewTransaction, String> methodTablecolumn;
    @FXML
    private TableColumn<TableviewTransaction, String> sendTablecolumn;
    @FXML
    private TableColumn<TableviewTransaction, String> sendMethodTablecolumn;
    @FXML
    private TableColumn<TableviewTransaction, Double> rate;
    @FXML
    private TableColumn<TableviewTransaction, Double> profit;


    @FXML
    private Pane borderPane;
}
