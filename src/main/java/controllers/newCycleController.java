package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.dao.ClientsDAO;
import main.java.entities.Clients;
import main.java.util.ConnectionPool;
import main.java.util.DatabaseUtils;
import main.java.util.TimeZone;
import main.java.util.ULID;
import main.java.util.monedas.MoneyType;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class newCycleController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewId.setVisible(false);

        try {
            bankCodes = new BanksDAO().getInfoOf("codigo", "moneda", "VES");
            receivedComboBox.getItems().addAll(bankCodes);
            bankCodes = new BanksDAO().getInfoOf("codigo", "moneda", "USDT");
            sentComboBox.getItems().addAll(bankCodes);
            bankCodes = new BanksDAO().getInfoOf("codigo", "moneda", "USD");
            sentComboBox.getItems().addAll(bankCodes);

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
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

        sentTextField.textProperty().addListener((_, _, newValue) -> {
            if (newValue.isEmpty() || rate.getText().isEmpty() || newValue == null || rate.getText() == null) {
                amountToReceive.setText("0.00");
                return;
            }

            try {
                double rateValue = Double.parseDouble(rate.getText());
                amountToReceive.setText(String.valueOf(Double.parseDouble(newValue) * rateValue));
            } catch (NumberFormatException e) {
                sentTextField.setText(newValue.substring(0, newValue.length() - 1));
                return;
            }
        });

        rate.textProperty().addListener((_, _, newValue) -> {
            if (newValue.isEmpty() || sentTextField.getText().isEmpty() || newValue == null || sentTextField.getText() == null) {
                amountToReceive.setText("0.00");
                return;
            } 

            try {
                double sentValue = Double.parseDouble(sentTextField.getText());
                amountToReceive.setText(String.valueOf(Double.parseDouble(newValue) * sentValue));
            } catch (NumberFormatException e) {
                rate.setText(newValue.substring(0, newValue.length() - 1));
                return;
            }
        });

        confirmButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                try {
                    final Connection conn = ConnectionPool.getConnection();
                    final BanksDAO banksDAO = new BanksDAO();
                    final DatabaseUtils dbUtils = new DatabaseUtils(conn);

                    final String sent = sentTextField.getText();
                    final String rateValue = rate.getText();
                    final String amount = amountToReceive.getText();
                    final String ref = refTextField.getText();
                    final String bankReceived = receivedComboBox.getValue();
                    final String bankSent = sentComboBox.getValue();
                    final String clientID = searchClientsBar.getText().split(" ")[0];
                    String bankSentTypeMoney = (String)dbUtils.getValueOf("moneda", "bancos", "codigo = " + "'" + bankSent + "'");
                    
                    if (sent.isEmpty() || rateValue.isEmpty() || amount.isEmpty() || ref.isEmpty() || bankReceived == null) {
                        return;
                    }

                    byte[] entropy = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
                    final String id = ULID.generate(System.currentTimeMillis(), entropy);
                    
                    final String query = "insert into cicles(id, cedula_cliente, admin, fecha, cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ref_bancaria) " + 
                                        "values (" + "'" + id + "', " + "'" + clientID + "', " + "'" + Main.getUsername() + "', " +  "'" + TimeZone.getDateZoneCaracas() + "', " +
                                        "'" + Double.parseDouble(amount) + "', " + "'" + MoneyType.BOLIVARES.getNombre() + "', " + "'" + bankReceived + "', " + "'" + sent + "', " + "'" + bankSentTypeMoney + "', " + "'" + bankSent + "', " + "'ACTIVE', " + "'" + rateValue + "', " + "'" + ref + "');";
            
                    final Double totalBalanceAccount =  banksDAO.getTotalBalanceOf(bankSent);
                    final Double totalBalanceAccountReceived = banksDAO.getTotalBalanceOf(bankReceived);

                    if (totalBalanceAccount <= 0.0 || totalBalanceAccount < Double.parseDouble(sent)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "No hay suficiente saldo en la cuenta para realizar la transacción.", ButtonType.CLOSE);
                        alert.showAndWait();
                        return;
                    }

                    try (final PreparedStatement st = conn.prepareStatement(query)) {
                        st.executeUpdate();
                        final String dateTimeQuery = "select fecha, hora from cicles order by id desc limit 1";

                        try (final PreparedStatement stm = conn.prepareStatement(dateTimeQuery)) {
                            ResultSet rs = stm.executeQuery();
                            if (rs.next()) {
                                dbUtils.updateRegister("bancos", "saldo_actual", (totalBalanceAccount - Double.parseDouble(sent)), "codigo = " + "'" + bankSent + "'");
                                dbUtils.updateRegister("bancos", "saldo_actual", Double.parseDouble(amount) + totalBalanceAccountReceived, "codigo = " + "'" + bankReceived + "'");
                            } else {
                                System.err.println("No data found in cicles table.");
                            }

                            stm.close();
                        }

                        conn.close();
                        st.close();
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Transacción realizada con éxito", ButtonType.CLOSE);
                    alert.showAndWait();
                    
                    Main.switchToDashboard();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                try {
                    clearfields();
                    Main.switchToDashboard();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearfields() {
        sentTextField.clear();
        rate.clear();
        amountToReceive.clear();
        refTextField.clear();
        receivedComboBox.getSelectionModel().clearSelection();
        sentComboBox.getSelectionModel().clearSelection();
        searchClientsBar.clear();
    }

    @FXML
    private TextField sentTextField;
    @FXML
    private TextField rate;
    @FXML
    private TextField amountToReceive;
    @FXML
    private TextField refTextField;
    @FXML
    private ComboBox<String> receivedComboBox;
    @FXML
    private ComboBox<String> sentComboBox;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private List<String> bankCodes;
    @FXML
    private ListView<String> listViewId;
    @FXML
    ObservableList<Clients> clientsList = FXCollections.observableArrayList();
    @FXML
    private TextField searchClientsBar;
    
}
