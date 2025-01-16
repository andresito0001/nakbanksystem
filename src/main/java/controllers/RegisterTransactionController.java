package main.java.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import main.java.dao.BanksDAO;
import main.java.dao.ClientsDAO;
import main.java.entities.Clients;
import main.java.util.ConnectionPool;

public class RegisterTransactionController{
    @FXML
    private ListView<String> listViewId;
    @FXML
    private TextField searchClientsBar;
    @FXML
    ObservableList<Clients> clientsList = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> receivedCheckBox;
    @FXML
    private ComboBox<String> checkboxSent;
    @FXML
    private ComboBox<String> typeTransBox;
    @FXML
    private TextField receivedTextField;
    @FXML
    private TextField sentTextField;
    @FXML
    private TextField amountTextField;
    @FXML
    private TextField initialPaymentTextField;
    @FXML
    List<String> bankCodes = new ArrayList<>();
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private CheckBox fullPayment;
    
    @FXML
    public void initialize() throws SQLException {
        listViewId.setVisible(false);

        receivedCheckBox.getItems().add("DESCONOCIDO");
        
        bankCodes = new BanksDAO(ConnectionPool.getConnection()).getInfoOf("codigo", null, null);
        receivedCheckBox.getItems().addAll(bankCodes);
        checkboxSent.getItems().addAll(bankCodes);
        typeTransBox.getItems().addAll("COMPRA", "SWAP");

        receivedCheckBox.setOnAction(this::getReceivedBank);
        checkboxSent.setOnAction(this::getSentBank);
        typeTransBox.setOnAction(this::getTypeTrans);

        ClientsDAO clientsDAO = new ClientsDAO(ConnectionPool.getConnection());
        List<Clients> clients = clientsDAO.getClientsAsList();
        clientsList.addAll(clients);

        FilteredList<Clients> filteredClients = new FilteredList<>(clientsList, _ -> true);

        searchClientsBar.textProperty().addListener((_, _, newValue) -> {
            filteredClients.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    listViewId.setVisible(false);
                    return false;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return client.getName().toLowerCase().contains(lowerCaseFilter) ||
                       client.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                       client.getCi().toLowerCase().contains(lowerCaseFilter) ||
                       client.getAlias().toLowerCase().contains(lowerCaseFilter);
            });

            listViewId.setVisible(!newValue.isEmpty());

            listViewId.setItems(FXCollections.observableArrayList(
                filteredClients.stream()
                    .map(client -> String.format("%s %s %s %s",
                        client.getName(),
                        client.getLastName(),
                        client.getCi(),
                        client.getAlias()))
                    .toList()
            ));
        });

        listViewId.setItems(FXCollections.observableArrayList(
            clientsList.stream()
                .map(client -> String.format("%s %s %s %s",
                    client.getName(),
                    client.getLastName(),
                    client.getCi(),
                    client.getAlias()))
                .toList()
        ));

        searchClientsBar.focusedProperty().addListener((_, _, newValue) -> {
            if (newValue && !searchClientsBar.getText().isEmpty()) {
                listViewId.setVisible(!searchClientsBar.getText().isEmpty());
            } else {
                listViewId.setVisible(false);
            }
        });

        listViewId.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null && !listViewId.getItems().isEmpty()) {
                searchClientsBar.setText(newValue.toString());
                // System.out.println("Selected client: " + newValue);
                // listViewId.setVisible(false);
            }
        });


        confirmButton.setOnAction(_ -> {
            try {
                registerTransaction();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        fullPayment.setOnAction(_ -> {
            if (fullPayment.isSelected()) {
                initialPaymentTextField.setDisable(true);
            } else {
                initialPaymentTextField.setDisable(false);
            }
        });

        cancelButton.setOnAction(_ -> clearFields());
    }

    private void registerTransaction() throws SQLException {
        String sentBank = checkboxSent.getValue();
        String receivedBank = receivedCheckBox.getValue();
        String transactionType = typeTransBox.getValue();
        String sent = sentTextField.getText();
        String received = receivedTextField.getText();
        String amount = amountTextField.getText();
        String client = searchClientsBar.getText();
        List<String> clientSplit = List.of(client.split(" "));
        String ID  = clientSplit.get(2);
        
        
        // System.out.println("Sent bank: " + sentBank + "\nReceived bank: " + receivedBank + "\nTransaction type: " + transactionType + "\nSent: " + sent + "\nReceived: " + received + "\nAmount: " + amount + "\nClient: " + ID);
        clearFields();
    }

    public String getReceivedBank(ActionEvent event) {
        return receivedCheckBox.getValue();
    }

    public String getSentBank(ActionEvent event) {
        return checkboxSent.getValue();
    }

    public String getTypeTrans(ActionEvent event) {
        return typeTransBox.getValue();
    }

    private void clearFields() {
        sentTextField.clear();
        receivedTextField.clear();
        amountTextField.clear();
        checkboxSent.setValue(null);
        receivedCheckBox.setValue(null);
        typeTransBox.setValue(null);
    }
}