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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.transformation.FilteredList;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.dao.ClientsDAO;
import main.java.dao.InventoryDAO;
import main.java.dao.TransactionsDAO;
import main.java.entities.Banks;
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

        BanksDAO banksDAO = new BanksDAO();
        List<Banks> bancosRecibidos = new ArrayList<>();
        List<Banks> bancosEnviados = new ArrayList<>();

        banksDAO.setBank(bancosRecibidos, null, null);
        banksDAO.setBank(bancosEnviados, null, null);
        
        //bankCodes = new BanksDAO().getInfoOf("codigo", null, null);
        //receivedComboBox.getItems().addAll(bankCodes);
        //sentComboBox.getItems().addAll(bankCodes);

        receivedComboBox.getItems().addAll(bancosRecibidos);
        sentComboBox.getItems().addAll(bancosEnviados);
        crearComponentes();

        typeTransComboBox.getItems().addAll("COMPRA", "SWAP");
        swapComboBox.getItems().addAll("CLIENTE", "EMPRESA");
        swapComboBox.setLayoutX(234);
        swapComboBox.setLayoutY(346);
        swapComboBox.setPrefWidth(165);
        swapComboBox.setPrefHeight(28);

        swapComboBox.setVisible(false);

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

        swapCommission.setOnAction(_ -> {
            if (swapCommission.isSelected()) {
                swapCommissionTextField.setDisable(false);
                swapCommissionLabel.setDisable(false);
                swapComboBox.setDisable(false);
                amountLabel.setDisable(false);
            } else {
                swapCommissionTextField.setDisable(true);
                swapCommissionLabel.setDisable(true);
                swapComboBox.setDisable(true);
                amountLabel.setDisable(true);
            }
        });

        cancelButton.setOnAction(_ -> Main.switchToDashboard());

        typeTransComboBox.setOnAction(_ -> {            
            if (typeTransComboBox.getValue().equals("COMPRA")) {
                sentTextField.setEditable(false);
                swapComboBox.setVisible(false);

                swapCommissionTextField.setVisible(false);
                swapCommissionLabel.setVisible(false);
                swapCommission.setVisible(false);

                amountTextField.setVisible(true);
                amountTextField.setDisable(false);
                amountLabel.setVisible(true);
                amountLabel.setDisable(false);
                amountLabel.setText("Tasa $");

                mobilePaymentCheckBox.setVisible(true);
                mobilePaymentCheckBox.setDisable(false);
                mobilePaymentTextField.setVisible(true);
                mobilePaymentTextField.setDisable(true);
                mobilePaymentLabel.setVisible(true);
                mobilePaymentLabel.setDisable(true);
                mobilePaymentCheckBox.setSelected(false);
                
            } else if (typeTransComboBox.getValue().equals("SWAP")) {
                sentTextField.setEditable(true);
                swapComboBox.setVisible(true);
                swapComboBox.setDisable(true);

                amountTextField.setVisible(false);

                amountLabel.setVisible(true);
                amountLabel.setDisable(true);
                amountLabel.setText("Comision pagada por: ");
            
                swapCommissionTextField.setVisible(true);
                swapCommissionTextField.setDisable(true);
                swapCommissionLabel.setVisible(true);
                swapCommissionLabel.setDisable(true);
                swapCommission.setVisible(true);
                swapCommission.setDisable(false);
                
                mobilePaymentTextField.setVisible(false);
                mobilePaymentLabel.setVisible(false);
                mobilePaymentCheckBox.setVisible(false);

            } else { throw new IllegalArgumentException("Invalid transaction type"); }
        });

        receivedTextField.textProperty().addListener((_, _, newValue) -> {
            try {
                if (!amountTextField.getText().isEmpty() && amountTextField != null) {
                    sentTextField.setText(String.valueOf(Double.parseDouble(newValue) * Double.parseDouble(amountTextField.getText())));
                }
            } catch (NumberFormatException e) {
                sentTextField.clear();
            }
        });

        amountTextField.textProperty().addListener((_, _, newValue) -> {
            try {
                if (!receivedTextField.getText().isEmpty() && receivedTextField != null) {
                    sentTextField.setText(String.valueOf(Double.parseDouble(receivedTextField.getText()) * Double.parseDouble(newValue)));
                }
            } catch (NumberFormatException e) {
                sentTextField.clear();
            }
        });

        mobilePaymentTextField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!newValue.matches("^\\d*\\.?\\d*$")) {
                    mobilePaymentTextField.setText(oldValue);
                } else {
                    try {
                        double value = Double.parseDouble(newValue);
                        
                        if (value < 0 || value > 100) {
                            mobilePaymentTextField.setText(oldValue);
                        }

                        if (newValue.contains(".") && newValue.split("\\.")[1].length() > 2) {
                            mobilePaymentTextField.setText(oldValue);
                        }
                        
                    } catch (NumberFormatException e) {
                        mobilePaymentTextField.clear();
                    }
                }
            }
        });

        
        swapCommissionTextField.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!newValue.matches("^\\d*\\.?\\d*$")) {
                    swapCommissionTextField.setText(oldValue);
                } else {
                    try {
                        double value = Double.parseDouble(newValue);
                        
                        if (value < 0 || value > 100) {
                            swapCommissionTextField.setText(oldValue);
                        }

                        if (newValue.contains(".") && newValue.split("\\.")[1].length() > 2) {
                            swapCommissionTextField.setText(oldValue);
                        }

                        final String commissionBy = swapComboBox.getSelectionModel().getSelectedItem();
                        if (commissionBy.equals("CLIENTE")) {
                            final Double receivedValue = Double.parseDouble(receivedTextField.getText());
                            receivedTextField.setText(String.valueOf(receivedValue + (receivedValue * value / 100)));
                        } else if (commissionBy.equals("EMPRESA")) {
                            final Double sent = Double.parseDouble(sentTextField.getText());
                            sentTextField.setText(String.valueOf(sent + (sent * value / 100)));
                        }

                    } catch (NumberFormatException e) {
                        swapCommissionTextField.clear();
                    }
                }
            } else {
                receivedTextField.clear();
                sentTextField.clear();
            }
        });

    }

    public void crearComponentes () {
        receivedComboBox.setButtonCell(new ListCell<Banks>() {
            @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                        if (banco != null) {
                            setText(banco.getCodigo().concat(" (").concat(banco.getMoneda()).concat(")"));
                        }
                        else 
                            setText(null);
                }
        });

        receivedComboBox.setCellFactory((ListView<Banks> _) -> {
            final ListCell<Banks> listCell = new ListCell<>() {
                @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                     if (banco != null) {
                        setText(banco.getCodigo().concat(" (").concat(banco.getMoneda()).concat(")"));
                     }
                     else 
                        setText(null);
                }
            };
            return listCell;
        });

        sentComboBox.setButtonCell(new ListCell<Banks>() {
            @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                        if (banco != null) {
                            setText(banco.getCodigo().concat(" (").concat(banco.getMoneda()).concat(")"));

                        }
                        else 
                            setText(null);
                }
        });

        sentComboBox.setCellFactory((ListView<Banks> _) -> {
            final ListCell<Banks> listCell = new ListCell<>() {
                @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                    if (banco != null) {
                        setText(banco.getCodigo().concat(" (").concat(banco.getMoneda()).concat(")"));
                        saldoEnviadoDisp.setText("Saldo disponible: " + banco.getSaldo());    
                    }
                    else {
                        setText(null);
                        saldoEnviadoDisp.setText(null);
                    }
                }
            };
            return listCell;
        });
    }
    
    private void registerTransaction() throws SQLException {
        amountLabel.setText("Tasa $");
        DatabaseUtils databaseUtils = new DatabaseUtils();
        ClientsDAO clientsDAO = new ClientsDAO();
        final String typeTrans = typeTransComboBox.getValue();

        switch (typeTrans) {
            case "COMPRA" : {
                double saldoBanco = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                double montoRequerido = !initialPaymentClientCehckbox.isSelected() 
                                        ? Double.parseDouble(initialPaymentClientTextfield.getText())
                                        : Double.parseDouble(sentTextField.getText());

                if (saldoBanco < montoRequerido) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("The bank does not have enough money");
                    alert.showAndWait();

                    throw new IllegalArgumentException("The bank does not have enough money");
                }

                final String clientID = searchClientsBar.getText().split(" ")[0];
                final Clients client = clientsDAO.getCLientBy("where cedula = " + "'" + clientID + "'");

                final String bankRecived = receivedComboBox.getSelectionModel().getSelectedItem().getCodigo();
                final String bankSent = sentComboBox.getSelectionModel().getSelectedItem().getCodigo();
                String moneyTypeSent = sentComboBox.getSelectionModel().getSelectedItem().getMoneda();
                String moneyTypeReceived = receivedComboBox.getSelectionModel().getSelectedItem().getMoneda();

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

                Double amountInitialPayment = 0.0,
                amountInitialPaymentClient = 0.0;

                final Transactions transaction = new Transactions (
                    id, cycleId, client, Main.getUsername(), date, time,
                    typeTrans, received, moneyTypeReceived, bankRecived,
                    sent, moneyTypeSent, bankSent, null, ammonut, 
                    gananciaPerdida, ref, cycleStatus
                );

                TransactionsDAO transactionsDAO = new TransactionsDAO();
                InventoryDAO inventoryDAO = new InventoryDAO();

                if (fullPaymentReceiver.isSelected() && initialPaymentClientCehckbox.isSelected()) {
                    // REGISTRAR TRANSACCION
                    transaction.setStatus("OK");
                    transactionsDAO.newTransaction(transaction);

                    // INGRESO
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "INGRESO",
                        received,
                        transaction.getCurrencyReceived(),
                        transaction.getReceivedMethod(),
                        typeTrans,
                        transaction.getTime(),
                        transaction.getId()
                    ));
                    
                    // EGRESO
                    inventoryDAO.newRegister(new Inventory(
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "EGRESO",
                        sent,
                        transaction.getSentCurrency(),
                        transaction.getSentMethod(),
                        typeTrans,
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // ACTUALIZAR BANCO ENVIADO
                    Double saldoBancoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceEnviado = saldoBancoEnviado - sent;
                    
                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                    
                    // ACTUALIZAR BANCO RECIBIDO
                    Double saldoBancoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceRecibido = saldoBancoRecibido + received;

                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");

                } else if (!fullPaymentReceiver.isSelected() && !initialPaymentClientCehckbox.isSelected()) {
                    amountInitialPayment = Double.parseDouble(initialPaymentReceiverTextField.getText());
                    amountInitialPaymentClient = Double.parseDouble(initialPaymentClientTextfield.getText());

                    //REGISTRAR TRANSACCION
                    transaction.setStatus("PENDIENTE");
                    transactionsDAO.newTransaction(transaction);
                    
                    // EGRESO ABONADO POR EMPRESA
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "EGRESO",
                        amountInitialPaymentClient,
                        transaction.getSentCurrency(),
                        transaction.getSentMethod(),
                        "ABONO",
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // INGRESO ABONADO POR CLIENTE
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "INGRESO",
                        amountInitialPayment,
                        transaction.getCurrencyReceived(),
                        transaction.getReceivedMethod(),
                        "ABONO",
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // ACTUALIZAR BANCO ENVIADO
                    Double saldoBancoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceEnviado = saldoBancoEnviado - amountInitialPaymentClient;
                    
                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                    
                    // ACTUALIZAR BANCO RECIBIDO
                    Double saldoBancoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceRecibido = saldoBancoRecibido + amountInitialPayment;

                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");

                } else if (fullPaymentReceiver.isSelected() && !initialPaymentClientCehckbox.isSelected()) {
                    amountInitialPaymentClient = Double.parseDouble(initialPaymentClientTextfield.getText());
 
                    // REGISTRAR TRANSACCION
                    transaction.setStatus("RECIBIDO");
                    transactionsDAO.newTransaction(transaction);

                    // EGRESO ABONADO POR EMPRESA
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "EGRESO",
                        amountInitialPaymentClient,
                        transaction.getSentCurrency(),
                        transaction.getSentMethod(),
                        "ABONO",
                        transaction.getTime(),
                        transaction.getId()
                    ));
                    
                    // INGRESO POR CLIENTE
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "INGRESO",
                        received,
                        transaction.getCurrencyReceived(),
                        transaction.getReceivedMethod(),
                        "COMPRA",
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // ACTUALIZAR BANCO ENVIADO
                    Double saldoBancoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceEnviado = saldoBancoEnviado - amountInitialPaymentClient;
                    
                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                    
                    // ACTUALIZAR BANCO RECIBIDO
                    Double saldoBancoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceRecibido = saldoBancoRecibido + received;

                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                
                } else {
                    amountInitialPayment = Double.parseDouble(initialPaymentReceiverTextField.getText());
          
                    // REGISTRAR TRANSACCION
                    transaction.setStatus("ENVIADO");
                    transactionsDAO.newTransaction(transaction);

                    // EGRESO POR EMPRESA
                    inventoryDAO.newRegister(new Inventory(
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "EGRESO",
                        sent,
                        transaction.getSentCurrency(),
                        transaction.getSentMethod(),
                        "COMPRA",
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // INGRESO ABONADO POR CLIENTE
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "INGRESO",
                        amountInitialPayment,
                        transaction.getCurrencyReceived(),
                        transaction.getReceivedMethod(),
                        "ABONO",
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // ACTUALIZAR BANCO ENVIADO
                    Double saldoBancoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceEnviado = saldoBancoEnviado - sent;
                    
                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                    
                    // ACTUALIZAR BANCO RECIBIDO
                    Double saldoBancoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceRecibido = saldoBancoRecibido + amountInitialPayment;

                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                }
                
                if (mobilePaymentCheckBox.isSelected()) {
                    Double mobilePaymentValue = 0.00;
                    if (!initialPaymentClientCehckbox.isSelected() && !initialPaymentClientTextfield.getText().isEmpty()) {
                        mobilePaymentValue = Double.parseDouble(initialPaymentClientTextfield.getText()) * (Double.parseDouble(mobilePaymentTextField.getText()) / 100);
                    } else {
                        mobilePaymentValue = Double.parseDouble(sentTextField.getText()) * (Double.parseDouble(mobilePaymentTextField.getText()) / 100);
                    }

                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "EGRESO",
                        mobilePaymentValue,
                        sentComboBox.getSelectionModel().getSelectedItem().getMoneda(),
                        sentComboBox.getSelectionModel().getSelectedItem().getCodigo(),
                        "GASTO",
                        transaction.getTime(),
                        transaction.getId()
                    ));
                }
            } break;
            case "SWAP" : {
                double saldoBanco = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                double montoRequerido = !initialPaymentClientCehckbox.isSelected() 
                                        ? Double.parseDouble(initialPaymentClientTextfield.getText())
                                        : Double.parseDouble(sentTextField.getText());
                
                if (saldoBanco < montoRequerido) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("The bank does not have enough money");
                    alert.showAndWait();

                    throw new IllegalArgumentException("The bank does not have enough money");
                }

                final String clientID = searchClientsBar.getText().split(" ")[0];
                final String cycleId = databaseUtils.getInfoByLastReferenceOf("ciclos", "id", null, null);
                final byte[] entropy = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };

                Transactions transaction = new Transactions (
                    ULID.generate(System.currentTimeMillis(), entropy),
                    cycleId,
                    clientsDAO.getCLientBy("where cedula = " + "'" + clientID + "'"),
                    Main.getUsername(),
                    main.java.util.TimeZone.getDateZoneCaracas(),
                    main.java.util.TimeZone.getTimeZoneCaracas(),
                    typeTransComboBox.getValue(),
                    Double.parseDouble(receivedTextField.getText()),
                    receivedComboBox.getSelectionModel().getSelectedItem().getMoneda(),
                    receivedComboBox.getSelectionModel().getSelectedItem().getCodigo(),
                    Double.parseDouble(sentTextField.getText()),
                    sentComboBox.getSelectionModel().getSelectedItem().getMoneda(),
                    sentComboBox.getSelectionModel().getSelectedItem().getCodigo(),
                    null,
                    0.0,
                    0.0,
                    refTextField.getText(),
                    databaseUtils.getValueOf("status_recepcion", "ciclos", "id = '"+cycleId+"'").toString()
                );

                TransactionsDAO transactionsDAO = new TransactionsDAO();
                InventoryDAO inventoryDAO = new InventoryDAO();

                if (fullPaymentReceiver.isSelected() && initialPaymentClientCehckbox.isSelected()) {
                    // REGISTRAR TRANSACCION
                    transaction.setStatus("OK");
                    transactionsDAO.newTransaction(transaction);

                    // INGRESO
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "INGRESO",
                        transaction.getQuantityReceived(),
                        transaction.getCurrencyReceived(),
                        transaction.getReceivedMethod(),
                        transaction.getType(),
                        transaction.getTime(),
                        transaction.getId()
                    ));
                    
                    // EGRESO
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "EGRESO",
                        transaction.getSentQuantity(),
                        transaction.getSentCurrency(),
                        transaction.getSentMethod(),
                        transaction.getType(),
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // ACTUALIZAR BANCO ENVIADO
                    Double saldoBancoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceEnviado = saldoBancoEnviado - transaction.getSentQuantity();
                    
                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                    
                    // ACTUALIZAR BANCO RECIBIDO
                    Double saldoBancoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceRecibido = saldoBancoRecibido + transaction.getQuantityReceived();

                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");

                } else if (!fullPaymentReceiver.isSelected() && !initialPaymentClientCehckbox.isSelected()) {
                    // status = "PENDIENTE";
                    final Double amountInitialPayment = Double.parseDouble(initialPaymentReceiverTextField.getText());
                    final Double amountInitialPaymentClient = Double.parseDouble(initialPaymentClientTextfield.getText());

                    //REGISTRAR TRANSACCION
                    transaction.setStatus("PENDIENTE");
                    transactionsDAO.newTransaction(transaction);
                    
                    // EGRESO ABONADO POR EMPRESA
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "EGRESO",
                        amountInitialPaymentClient,
                        transaction.getSentCurrency(),
                        transaction.getSentMethod(),
                        "ABONO",
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // INGRESO ABONADO POR CLIENTE
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "INGRESO",
                        amountInitialPayment,
                        transaction.getCurrencyReceived(),
                        transaction.getReceivedMethod(),
                        "ABONO",
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // ACTUALIZAR BANCO ENVIADO
                    Double saldoBancoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceEnviado = saldoBancoEnviado - amountInitialPaymentClient;
                    
                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                    
                    // ACTUALIZAR BANCO RECIBIDO
                    Double saldoBancoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceRecibido = saldoBancoRecibido + amountInitialPayment;

                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");

                } else if (fullPaymentReceiver.isSelected() && !initialPaymentClientCehckbox.isSelected()) {
                    final Double amountInitialPaymentClient = Double.parseDouble(initialPaymentClientTextfield.getText());
 
                    // REGISTRAR TRANSACCION
                    transaction.setStatus("RECIBIDO");
                    transactionsDAO.newTransaction(transaction);

                    // EGRESO ABONADO POR EMPRESA
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "EGRESO",
                        amountInitialPaymentClient,
                        transaction.getSentCurrency(),
                        transaction.getSentMethod(),
                        "ABONO",
                        transaction.getTime(),
                        transaction.getId()
                    ));
                    
                    // INGRESO POR CLIENTE
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "INGRESO",
                        transaction.getQuantityReceived(),
                        transaction.getCurrencyReceived(),
                        transaction.getReceivedMethod(),
                        transaction.getType(),
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // ACTUALIZAR BANCO ENVIADO
                    Double saldoBancoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceEnviado = saldoBancoEnviado - amountInitialPaymentClient;
                    
                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                    
                    // ACTUALIZAR BANCO RECIBIDO
                    Double saldoBancoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceRecibido = saldoBancoRecibido + transaction.getQuantityReceived();

                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                } else {
                    final Double amountInitialPayment = Double.parseDouble(initialPaymentReceiverTextField.getText());
          
                    // REGISTRAR TRANSACCION
                    transaction.setStatus("ENVIADO");
                    transactionsDAO.newTransaction(transaction);

                    // EGRESO POR EMPRESA
                    inventoryDAO.newRegister(new Inventory(
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "EGRESO",
                        transaction.getSentQuantity(),
                        transaction.getSentCurrency(),
                        transaction.getSentMethod(),
                        transaction.getType(),
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // INGRESO ABONADO POR CLIENTE
                    inventoryDAO.newRegister(new Inventory (
                        ULID.generate(System.currentTimeMillis(), entropy),
                        transaction.getDate(),
                        "INGRESO",
                        amountInitialPayment,
                        transaction.getCurrencyReceived(),
                        transaction.getReceivedMethod(),
                        "ABONO",
                        transaction.getTime(),
                        transaction.getId()
                    ));

                    // ACTUALIZAR BANCO ENVIADO
                    Double saldoBancoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceEnviado = saldoBancoEnviado - transaction.getSentQuantity();
                    
                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                    
                    // ACTUALIZAR BANCO RECIBIDO
                    Double saldoBancoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo();
                    Double balanceRecibido = saldoBancoRecibido + amountInitialPayment;

                    databaseUtils.updateRegister("bancos", "saldo_actual", balanceRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                }
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
        
        clearFields();
        Main.switchToDashboard();
    }

    private void clearFields() {
        sentTextField.clear();
        receivedTextField.clear();
        amountTextField.clear();
        sentComboBox.setValue(null);
        receivedComboBox.setValue(null);
    }

    private void diableFields() {
        amountTextField.setVisible(false);
        amountLabel.setVisible(false);

        swapCommissionTextField.setVisible(false);
        swapCommissionLabel.setVisible(false);
        swapCommission.setVisible(false);

        mobilePaymentLabel.setVisible(false);
        mobilePaymentTextField.setVisible(false);
        mobilePaymentCheckBox.setVisible(false);
    }

    @FXML
    private ComboBox<String> swapComboBox;
    @FXML
    private ListView<String> listViewId;
    @FXML
    private TextField searchClientsBar;
    @FXML
    ObservableList<Clients> clientsList = FXCollections.observableArrayList();
    @FXML
    private ComboBox<Banks> receivedComboBox;
    @FXML
    private ComboBox<Banks> sentComboBox;
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
    @FXML
    private CheckBox swapCommission;
    @FXML
    private Label saldoEnviadoDisp;
}