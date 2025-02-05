package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.java.dao.BanksDAO;
import main.java.dao.ClientsDAO;
import main.java.entities.Clients;
import main.java.util.ConnectionPool;
import main.java.util.TimeZone;
import main.java.util.ULID;
import main.java.util.monedas.MoneyType;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class newCycleController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewId.setVisible(false);

        try {
            bankCodes = new BanksDAO(ConnectionPool.getConnection()).getInfoOf("codigo", null, null);
            receivedCheckBox.getItems().addAll(bankCodes);

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
                    String sent = sentTextField.getText();
                    String rateValue = rate.getText();
                    String amount = amountToReceive.getText();
                    String ref = refTextField.getText();
                    String received = receivedCheckBox.getValue();
                    String clientString = searchClientsBar.getText();
                    List<String> clientSplit = List.of(clientString.split(" "));
                   
                    

                    System.out.println("Sent: " + sent + " Rate: " + rateValue + " Amount: " + amount + " Ref: " + ref + " Received: " + received);
                    if (sent.isEmpty() || rateValue.isEmpty() || amount.isEmpty() || ref.isEmpty() || received == null) {
                        return;
                    }

                    byte[] entropy = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
                    final String id = ULID.generate(System.currentTimeMillis(), entropy);
                    
                    // final String query = "insert into cicles(id, cedula_cliente, admin, fecha, cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ref_bancaria) " + 
                    //                     "values (" + "'" + id + "', " + "'V-11222599', " + "'" + username + "', " +  "'" + TimeZone.getDateZoneCaracas() + "', " +
                    //                     "'" + cantRecibida + "', " + "'" + MoneyType.BOLIVARES.getNombre() + "', " + "'" + nCuenta + "', " + "'" + cantEnviada + "', " + "'" + MoneyType.BINANCE_USDT.getNombre() + "', " + "'" + "BE-WN-0006" + "', " + "'ACTIVE', " + "'" + tasa + "', " + "'" + ref_bancaria + "');";
                    

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
    private ComboBox<String> receivedCheckBox;
    @FXML
    private Button confirmButton;
    @FXML
    private List<String> bankCodes;
    @FXML
    private ListView<String> listViewId;
    @FXML
    ObservableList<Clients> clientsList = FXCollections.observableArrayList();
    @FXML
    private TextField searchClientsBar;
}
