package main.java.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.dao.ClientsDAO;
import main.java.dao.InventoryDAO;
import main.java.dao.TransactionsDAO;
import main.java.entities.Clients;
import main.java.entities.Inventory;
import main.java.entities.Transactions;
import main.java.util.DatabaseUtils;
import main.java.util.ULID;
import main.java.util.CheckTypes.checkMoneyType;

public class RegisterTransactionController {
    @FXML
    public void initialize() throws SQLException {
        listViewId.setVisible(false);

        // receivedComboBox.getItems().add("DESCONOCIDO");
        
        bankCodes = new BanksDAO().getInfoOf("codigo", null, null);
        receivedComboBox.getItems().addAll(bankCodes);
        sentComboBox.getItems().addAll(bankCodes);
        typeTransComboBox.getItems().addAll("COMPRA", "SWAP");

        receivedComboBox.setOnAction(this::getReceivedBank);
        sentComboBox.setOnAction(this::getSentBank);
        typeTransComboBox.setOnAction(this::getTypeTrans);

        ClientsDAO clientsDAO = new ClientsDAO();
        List<Clients> clients = clientsDAO.getClientsAsList();
        clientsList.addAll(clients);

        FilteredList<Clients> filteredClients = new FilteredList<>(clientsList, _ -> true);

        diableFields();

        searchClientsBar.textProperty().addListener((_, _, newValue) -> {
            filteredClients.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    listViewId.setVisible(false);
                    return false;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return client.getCi().toLowerCase().contains(lowerCaseFilter) ||
                    client.getName().toLowerCase().contains(lowerCaseFilter) || 
                    client.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                    client.getAlias().toLowerCase().contains(lowerCaseFilter);
            });

            listViewId.setVisible(!newValue.isEmpty());

            listViewId.setItems(FXCollections.observableArrayList(
                filteredClients.stream()
                    .map(client -> String.format("%s %s %s %s",
                        client.getCi(),
                        client.getName(),
                        client.getLastName(),
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

        fullPaymentReceiver.setOnAction(_ -> {
            if (fullPaymentReceiver.isSelected()) {
                initialPaymentReceiverTextField.setDisable(true);
                initialPaymentReceiverLabel.setDisable(true);
            } else {
                initialPaymentReceiverTextField.setDisable(false);
                initialPaymentReceiverLabel.setDisable(false);
            }
        });

        initialPaymentClientCehckbox.setOnAction(_ -> {
            if (initialPaymentClientCehckbox.isSelected()) {
                initialPaymentClientTextfield.setDisable(true);
                initialPaymentClientLabel.setDisable(true);
            } else {
                initialPaymentClientTextfield.setDisable(false);
                initialPaymentClientLabel.setDisable(false);
            }
        });

        mobilePaymentCheckBox.setOnAction(_ -> {
            if (mobilePaymentCheckBox.isSelected()) {
                mobilePaymentTextField.setDisable(false);
                mobilePaymentLabel.setDisable(false);
            } else {
                mobilePaymentTextField.setDisable(true);
                mobilePaymentLabel.setDisable(true);
            }
        });

        cancelButton.setOnAction(_ -> Main.switchToDashboard());

        typeTransComboBox.setOnAction(_ -> {
            if (typeTransComboBox.getValue().equals("COMPRA")) {
                swapCommissionTextField.setDisable(true);
                swapCommissionLabel.setDisable(true);

                amountTextField.setDisable(false);
                amountLabel.setDisable(false);

                initialPaymentReceiverTextField.setDisable(false);
                fullPaymentReceiver.setDisable(false);
                initialPaymentReceiverLabel.setDisable(false);

                initialPaymentClientTextfield.setDisable(false);
                initialPaymentClientCehckbox.setDisable(false);
                initialPaymentClientLabel.setDisable(false);

                mobilePaymentCheckBox.setDisable(false);
                mobilePaymentCheckBox.setSelected(false);
    

            } else if (typeTransComboBox.getValue().equals("SWAP")) {
                amountTextField.setDisable(true);
                amountLabel.setDisable(true);

                swapCommissionTextField.setDisable(false);
                swapCommissionLabel.setDisable(false);

                mobilePaymentTextField.setDisable(true);
                mobilePaymentLabel.setDisable(true);
                mobilePaymentCheckBox.setDisable(true);
            } else { throw new IllegalArgumentException("Invalid transaction type"); }
        });
    }

    private void registerTransaction() throws SQLException {
        if (sentComboBox.getValue() == null || receivedComboBox.getValue() == null || typeTransComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("All fields must be filled");
            alert.showAndWait();

            throw new IllegalArgumentException("All fields must be filled");
        }

        DatabaseUtils databaseUtils = new DatabaseUtils();
        ClientsDAO clientsDAO = new ClientsDAO();
        final String typeTrans = typeTransComboBox.getValue();

        switch (typeTrans) {
            case "COMPRA" : {
                final String clientID = searchClientsBar.getText().split(" ")[0];
                final Clients client = clientsDAO.getCLientBy("where cedula = " + "'" + clientID + "'");

                final String bankRecived = receivedComboBox.getValue();
                final String bankSent = sentComboBox.getValue();
                String moneyTypeSent = (String)databaseUtils.getValueOf("moneda", "bancos", "codigo = " + "'" + bankSent + "'");
                String moneyTypeReceived = (String)databaseUtils.getValueOf("moneda", "bancos", "codigo = " + "'" + bankRecived + "'");
                String status = new String();

                if (bankRecived.equals(bankSent) || moneyTypeReceived.equals(moneyTypeSent)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("The banks must be different and the money types must be different");
                    alert.showAndWait();

                    throw new IllegalArgumentException("The banks must be different");
                }

                final String date = main.java.util.TimeZone.getDateZoneCaracas();
                final String time = main.java.util.TimeZone.getTimeZoneCaracas();
                
                final Double received = Double.parseDouble(receivedTextField.getText());
                final Double sent = Double.parseDouble(sentTextField.getText());
                final Double ammonut = Double.parseDouble(amountTextField.getText());
                final String ref = refTextField.getText();
                final Double tasaMadre = Double.parseDouble(databaseUtils.getInfoByLastReferenceOf("ciclos", "tasa", null, null));
                Double gananciaPerdida = received - (sent / tasaMadre);
                
                gananciaPerdida = gananciaPerdida < 0 ? gananciaPerdida * -1 : gananciaPerdida; 

                final String cycleId = databaseUtils.getInfoByLastReferenceOf("ciclos", "id", null, null);
                final String cycleStatus = databaseUtils.getValueOf("status_recepcion", "ciclos", "id = '"+cycleId+"'").toString();

                final byte[] entropy = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
                
                final String id = ULID.generate(System.currentTimeMillis(), entropy);
                String inventoryULID = ULID.generate(System.currentTimeMillis(), entropy);

                Double amountInitialPayment = 0.0;
                Double amountInitialPaymentClient = 0.0;
                Double mobilePayment = 0.0;

                if (fullPaymentReceiver.isSelected() && initialPaymentClientCehckbox.isSelected()) {
                    status = "OK";
                } else if (!fullPaymentReceiver.isSelected() && !initialPaymentClientCehckbox.isSelected()) {
                    status = "PENDIENTE";
                    amountInitialPayment = Double.parseDouble(initialPaymentReceiverTextField.getText());
                    amountInitialPaymentClient = Double.parseDouble(initialPaymentClientTextfield.getText());
                } else if (fullPaymentReceiver.isSelected() && !initialPaymentClientCehckbox.isSelected()) {
                    status = "RECIBIDO";
                    amountInitialPaymentClient = Double.parseDouble(initialPaymentClientTextfield.getText());
                } else {
                    status = "ENVIADO";
                    amountInitialPayment = Double.parseDouble(initialPaymentReceiverTextField.getText());
                }

                if (mobilePaymentCheckBox.isSelected()) {
                    mobilePayment = Double.parseDouble(mobilePaymentTextField.getText());
                }
                
                final Transactions transaction = new Transactions (
                    id, cycleId, client, Main.getUsername(), date, time,
                    typeTrans, received, moneyTypeReceived, bankRecived,
                    sent, moneyTypeSent, bankSent, status, ammonut, 
                    gananciaPerdida, ref, cycleStatus
                );

                final Inventory inventoryEntrance = new Inventory (
                    inventoryULID,
                    transaction.getDate(),
                    "INGRESO",
                    transaction.getQuantityReceived(),
                    transaction.getCurrencyReceived(),
                    transaction.getReceivedMethod(),
                    typeTrans,
                    transaction.getTime(),
                    transaction.getId()
                );

                inventoryULID = ULID.generate(System.currentTimeMillis(), entropy);

                final Inventory inventoryExit = new Inventory (
                    inventoryULID,
                    transaction.getDate(),
                    "EGRESO",
                    transaction.getSentQuantity(),
                    transaction.getSentCurrency(),
                    transaction.getSentMethod(),
                    typeTrans,
                    transaction.getTime(),
                    transaction.getId()
                );

                TransactionsDAO transactionsDAO = new TransactionsDAO();
                InventoryDAO inventoryDAO = new InventoryDAO();

                transactionsDAO.newTransaction(transaction);

                inventoryDAO.newRegister(inventoryEntrance);
                inventoryDAO.newRegister(inventoryExit);
            } break;
            case "SWAP" : {

            } break;
            default: {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Invalid transaction type");
                alert.showAndWait();

                throw new IllegalArgumentException("Invalid transaction type");
            }
        }
        
        // System.out.println("Sent bank: " + sentBank + "\nReceived bank: " + receivedBank + "\nTransaction type: " + transactionType + "\nSent: " + sent + "\nReceived: " + received + "\nAmount: " + amount + "\nClient: " + ID);
        clearFields();
        Main.switchToDashboard();
    }

    public String getReceivedBank(ActionEvent event) {
        return receivedComboBox.getValue();
    }

    public String getSentBank(ActionEvent event) {
        return sentComboBox.getValue();
    }

    public String getTypeTrans(ActionEvent event) {
        return typeTransComboBox.getValue();
    }

    private void clearFields() {
        sentTextField.clear();
        receivedTextField.clear();
        amountTextField.clear();
        sentComboBox.setValue(null);
        receivedComboBox.setValue(null);
        // typeTransComboBox.setValue(null);
    }


    private void diableFields() {
        amountLabel.setDisable(true);
        amountTextField.setDisable(true);
        swapCommissionLabel.setDisable(true);
        swapCommissionTextField.setDisable(true);
        fullPaymentReceiver.setDisable(true);
        initialPaymentReceiverLabel.setDisable(true);
        initialPaymentReceiverTextField.setDisable(true);
        initialPaymentClientLabel.setDisable(true);
        initialPaymentClientTextfield.setDisable(true);
        initialPaymentClientCehckbox.setDisable(true);
        mobilePaymentCheckBox.setDisable(true);
        mobilePaymentLabel.setDisable(true);
        mobilePaymentTextField.setDisable(true);
    }

    @FXML
    private ListView<String> listViewId;
    @FXML
    private TextField searchClientsBar;
    @FXML
    ObservableList<Clients> clientsList = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> receivedComboBox;
    @FXML
    private ComboBox<String> sentComboBox;
    @FXML
    private ComboBox<String> typeTransComboBox;
    @FXML
    private TextField receivedTextField;
    @FXML
    private TextField sentTextField;
    @FXML
    private Label amountLabel;
    @FXML
    private TextField amountTextField;
    @FXML
    private Label initialPaymentReceiverLabel;
    @FXML
    private TextField initialPaymentReceiverTextField;
    @FXML
    private CheckBox fullPaymentReceiver;
    @FXML
    private TextField initialPaymentClientTextfield;
    @FXML
    private CheckBox initialPaymentClientCehckbox;
    @FXML
    private Label initialPaymentClientLabel;
    @FXML
    List<String> bankCodes = new ArrayList<>();
    @FXML
    private final List<String> moneyTypes = checkMoneyType.getMoneyTypes();
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label swapCommissionLabel;
    @FXML
    private TextField swapCommissionTextField;
    @FXML
    private CheckBox mobilePaymentCheckBox;
    @FXML
    private Label mobilePaymentLabel;
    @FXML
    private TextField mobilePaymentTextField;
    @FXML
    private TextField refTextField;
}