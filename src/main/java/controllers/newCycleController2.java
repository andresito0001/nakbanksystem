package main.java.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.dao.CicleDAO;
import main.java.dao.ClientsDAO;
import main.java.dao.InventoryDAO;
import main.java.entities.Banks;
import main.java.entities.Clients;
import main.java.entities.Cycle;
import main.java.util.DatabaseUtils;
import main.java.util.TimeZone;
import main.java.util.ULID;

public class newCycleController2 {
    @FXML
    private ComboBox<Banks> receivedComboBox;
    @FXML
    private ComboBox<Banks> sentComboBox;
    @FXML
    private TextField sentTextField;
    @FXML
    private TextField rate;
    @FXML
    private TextField refTextField;
    @FXML
    private TextField amountToReceive;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ListView<String> listViewId;
    @FXML
    ObservableList<Clients> clientsList = FXCollections.observableArrayList();
    @FXML
    private TextField searchClientsBar;

    
    public void initialize() throws SQLException {
        listViewId.setVisible(false);
        BanksDAO banksDAO = new BanksDAO();

        List<Banks> banksListReceived = new ArrayList<>();
        List<Banks> banksListSent = new ArrayList<>();

            banksDAO.setBank(banksListReceived, "moneda", "VES"); //Bancos donde se recibe
            banksDAO.setBank(banksListSent, "moneda", "USD' or moneda = 'USDT"); // Bancos donde se envia

            receivedComboBox.getItems().addAll(banksListReceived);
            sentComboBox.getItems().addAll(banksListSent);

            formatComboBox();
            formatTextField();
            formatListView();

            ClientsDAO clientsDAO = new ClientsDAO();
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
                else 
                    searchClientsBar.setText("");
            });


    }

    public void formatComboBox() {
            receivedComboBox.setButtonCell(new ListCell<Banks>() {
            @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                        if (banco != null) {
                            setText(banco.getNombre());
                        }
                        else 
                            setText(null);
                }
        });

        sentComboBox.setButtonCell(new ListCell<Banks>() {
            @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                        if (banco != null) {
                            setText(banco.getNombre());
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
                        setText(banco.getCodigo());
                     }
                     else 
                        setText(null);
                }
            };
            return listCell;
        });

        sentComboBox.setCellFactory((ListView<Banks> _) -> {
            final ListCell<Banks> listCell = new ListCell<>() {
                @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                     if (banco != null) {
                        setText(banco.getCodigo());
                     }
                     else 
                        setText(null);
                }
            };
            return listCell;
        });
    }

    public void formatTextField () {

        rate.textProperty().addListener((_,_,_) -> {
            if (!rate.getText().isEmpty() && !sentTextField.getText().isEmpty()) {
                Double monto = Double.parseDouble(rate.getText().toString()) * Double.parseDouble(sentTextField.getText().toString());
                amountToReceive.setText(monto.toString());
            }
            else 
                amountToReceive.setText("");
        });
        sentTextField.textProperty().addListener((_,_,_) -> {
            if (!rate.getText().isEmpty() && !sentTextField.getText().isEmpty()) {
                Double monto = Double.parseDouble(rate.getText().toString()) * Double.parseDouble(sentTextField.getText().toString());
                amountToReceive.setText(monto.toString());
            }
            else 
                amountToReceive.setText("");
        });

    }
    
    public void registerCycle (MouseEvent event) throws SQLException {
        if (!sentTextField.getText().isEmpty() && 
        !sentComboBox.getSelectionModel().isEmpty() && 
        !receivedComboBox.getSelectionModel().isEmpty() &&
        !rate.getText().isEmpty()) {
            if (sentComboBox.getSelectionModel().getSelectedItem().getSaldo() <= 0 || sentComboBox.getSelectionModel().getSelectedItem().getSaldo() < Double.parseDouble(sentTextField.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No hay suficiente saldo en la cuenta para realizar la transacción.", ButtonType.CLOSE);
                alert.showAndWait();
            }

            else {
                InventoryDAO inventoryDAO = new InventoryDAO();
                DatabaseUtils databaseUtils = new DatabaseUtils();
                ClientsDAO clientsDAO = new ClientsDAO();

                Date fecha = Date.valueOf(LocalDate.parse(TimeZone.getDateZoneCaracas()));
                LocalTime hora = LocalTime.parse(TimeZone.getTimeZoneCaracas());
                LocalDateTime fechaHora = LocalDateTime.of(LocalDate.parse(TimeZone.getDateZoneCaracas()), hora);
                Timestamp timestamp = Timestamp.valueOf(fechaHora);
                final String time = main.java.util.TimeZone.getTimeZoneCaracas();

                byte[] random = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
                String id_ciclo = ULID.generate(System.currentTimeMillis(), random);
                
                Double cantidadEnviada = Double.parseDouble(sentTextField.getText().toString());
                Double cantidadRecibida = Double.parseDouble(amountToReceive.getText().toString());
                Double tasa = Double.parseDouble(rate.getText().toString());

                final String clientID = searchClientsBar.getText().split(" ")[0];
                final Clients client = clientsDAO.getCLientBy("where cedula = '" + clientID + "'");

                final Cycle cycle = new Cycle(
                    id_ciclo, client, Main.getUsername(), fecha.toString(), time, 
                    cantidadRecibida, receivedComboBox.getSelectionModel().getSelectedItem().getMoneda(), receivedComboBox.getSelectionModel().getSelectedItem().getCodigo(), 
                    cantidadEnviada, sentComboBox.getSelectionModel().getSelectedItem().getMoneda(), sentComboBox.getSelectionModel().getSelectedItem().getCodigo(), 
                    "ACTIVE", tasa, refTextField.getText(), "OK"
                );

                CicleDAO cicleDAO = new CicleDAO();
                cicleDAO.insertCycle(cycle);

                Double saldoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo() + cantidadRecibida;
                Double saldoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo() - cantidadEnviada;

                databaseUtils.updateRegister("bancos", "saldo_actual", saldoRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                databaseUtils.updateRegister("bancos", "saldo_actual", saldoEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                
                String id_inventario = ULID.generate(System.currentTimeMillis(), random);

                inventoryDAO.newRegister("I-" + id_inventario, fecha, timestamp, "INGRESO", cantidadRecibida, receivedComboBox.getSelectionModel().getSelectedItem().getMoneda().toString(), 
                receivedComboBox.getSelectionModel().getSelectedItem().getCodigo().toString(), "CICLO", id_ciclo);
                
                inventoryDAO.newRegister("E-" + id_inventario, fecha, timestamp, "EGRESO", cantidadEnviada, sentComboBox.getSelectionModel().getSelectedItem().getMoneda().toString(), 
                sentComboBox.getSelectionModel().getSelectedItem().getCodigo().toString(), "CICLO", id_ciclo);

                Alert alert = new Alert(AlertType.INFORMATION, "Ciclo creado con exito", ButtonType.CLOSE);
                alert.showAndWait();

            }
        }
    }

    public void formatListView() throws SQLException {
        ClientsDAO clientsDAO = new ClientsDAO();
        List<Clients> clients = clientsDAO.getClientsAsList();
        clientsList.addAll(clients);

    }

    public void clearfields() {
        sentTextField.clear();
        rate.clear();
        amountToReceive.clear();
        refTextField.clear();
        receivedComboBox.getSelectionModel().clearSelection();
        sentComboBox.getSelectionModel().clearSelection();
        searchClientsBar.clear();
    }

}
