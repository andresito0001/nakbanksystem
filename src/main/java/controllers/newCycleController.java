package main.java.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
import main.java.entities.Inventory;
import main.java.util.DatabaseUtils;
import main.java.util.SceneSwitcher;
import main.java.util.TimeZone;
import main.java.util.ULID;

public class newCycleController {
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
    @FXML
    private CheckBox pagadoCheck;
    @FXML
    private TextField abonado;
    @FXML
    private Label sentAmountLabel;
    @FXML
    private Label sentBankLabel;
    @FXML
    private Label refLabel;

    
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

            List<Clients> clientes = clientsDAO.getClientsAsList();
            ObservableList<Clients> clientsList = FXCollections.observableArrayList(clientes);

            FilteredList<Clients> filteredList = new FilteredList<>(clientsList, client -> true);

            searchClientsBar.textProperty().addListener((_, _, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    listViewId.setVisible(false);
                    return;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                filteredList.setPredicate(client -> 
                client.getCi().toLowerCase().contains(lowerCaseFilter) || 
                client.getName().toLowerCase().contains(lowerCaseFilter) ||
                client.getLastName().contains(lowerCaseFilter) ||
                client.getAlias().contains(lowerCaseFilter)
                );

                ObservableList<String> filteredItems = FXCollections.observableArrayList(
                    filteredList.stream()
                    .map(client -> String.format("%s %s %s %s", 
                    client.getCedula(),
                    client.getName(), 
                    client.getLastName(),
                    client.getAlias()))
                    .toList()
                );

                listViewId.setItems(filteredItems); 
                listViewId.setVisible(!filteredItems.isEmpty());
            });

            listViewId.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
                if (newValue != null && !listViewId.getItems().isEmpty()) {
                    searchClientsBar.setText(newValue);
                }
            });

            searchClientsBar.focusedProperty().addListener((_, _, newValue) -> {
                if (!newValue) {
                    listViewId.setVisible(false);
                }
            });


            pagadoCheck.setOnAction(_ -> {
                if (!pagadoCheck.isSelected()) {
                    abonado.setVisible(true);

                    receivedComboBox.setLayoutY(326);
                    amountToReceive.setLayoutY(326);
                    refTextField.setLayoutY(326);
                    refLabel.setLayoutY(308);
                    sentAmountLabel.setLayoutY(308);
                    sentBankLabel.setLayoutY(308);
                }
                else {
                    abonado.setVisible(false);

                    receivedComboBox.setLayoutY(309);
                    amountToReceive.setLayoutY(309);
                    refTextField.setLayoutY(309);
                    refLabel.setLayoutY(291);
                    sentAmountLabel.setLayoutY(291);
                    sentBankLabel.setLayoutY(291);

                }
            });


    }

    public void updateListView(ObservableList<Clients> clientsList) {
        if (clientsList.isEmpty()) {
            listViewId.setItems(FXCollections.observableArrayList());
            listViewId.setVisible(false);
        } else {
            listViewId.setItems(FXCollections.observableArrayList(
                clientsList.stream().
                map(client -> String.format("%s %s %s %s",
                client.getCedula(),
                client.getName(),
                client.getLastName(),
                client.getAlias()))
                .toList()
            ));
        }
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

        UnaryOperator <TextFormatter.Change> filterMonto = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*\\d*(\\.\\d{0,3})?")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> formatoMonto = new TextFormatter<>(filterMonto);
        TextFormatter<String> formatoTasa = new TextFormatter<>(filterMonto);
        TextFormatter<String> formatoAbonado = new TextFormatter<>(filterMonto);
        TextFormatter<String> formatoEnviado = new TextFormatter<>(filterMonto);

        amountToReceive.setTextFormatter(formatoMonto);
        rate.setTextFormatter(formatoTasa);
        abonado.setTextFormatter(formatoAbonado);
        sentTextField.setTextFormatter(formatoEnviado);
    }
    
    public void registerCycle (MouseEvent event) throws SQLException {
        if (!sentTextField.getText().isEmpty() && 
        !sentComboBox.getSelectionModel().isEmpty() && 
        !receivedComboBox.getSelectionModel().isEmpty() &&
        !rate.getText().isEmpty()) {
            if (
                (sentComboBox.getSelectionModel().getSelectedItem().getSaldo() <= 0 
                || sentComboBox.getSelectionModel().getSelectedItem().getSaldo() < Double.parseDouble(sentTextField.getText()) 
            ) && pagadoCheck.isSelected() ) {
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

                final String date = main.java.util.TimeZone.getDateZoneCaracas();
                final String time = main.java.util.TimeZone.getTimeZoneCaracas();

                byte[] random = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
                String id_ciclo = ULID.generate(System.currentTimeMillis(), random);
                
                Double cantidadEnviada = Double.parseDouble(sentTextField.getText().toString());
                Double cantidadRecibida = Double.parseDouble(amountToReceive.getText().toString());
                Double tasa = Double.parseDouble(rate.getText().toString());

                final String clientID = searchClientsBar.getText().split(" ")[0];
                final Clients client = clientsDAO.getCLientBy("where cedula = '" + clientID + "'");
                
                String statusRecepcion = "OK", tipoOperacion = "CICLO";

                if (!abonado.getText().isEmpty()) {
                    statusRecepcion = "RECIBIDO";
                    cantidadEnviada = Double.parseDouble(abonado.getText().toString());
                    tipoOperacion = "ABONO";
                } 
                else if (abonado.getText().isEmpty() && pagadoCheck.isSelected()) {
                    statusRecepcion = "RECIBIDO";
                    cantidadEnviada = 0.0;
                    tipoOperacion = "ABONO";
                }

                final Cycle cycle = new Cycle(
                    id_ciclo, client, Main.getUsername(), date, time, 
                    cantidadRecibida, receivedComboBox.getSelectionModel().getSelectedItem().getMoneda().toString(), receivedComboBox.getSelectionModel().getSelectedItem().getCodigo().toString(), 
                    Double.parseDouble(sentTextField.getText().toString()), sentComboBox.getSelectionModel().getSelectedItem().getMoneda().toString(), sentComboBox.getSelectionModel().getSelectedItem().getCodigo(), 
                    "ACTIVE", tasa, refTextField.getText().toString(), statusRecepcion
                );

                CicleDAO cicleDAO = new CicleDAO();
                cicleDAO.insertCycle(cycle);
                String id_inventario = ULID.generate(System.currentTimeMillis(), random);


                Double saldoRecibido = receivedComboBox.getSelectionModel().getSelectedItem().getSaldo() + cantidadRecibida;
                Double saldoEnviado = sentComboBox.getSelectionModel().getSelectedItem().getSaldo() - cantidadEnviada;

                databaseUtils.updateRegister("bancos", "saldo_actual", saldoRecibido, "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo().toString() + "'");
                databaseUtils.updateRegister("bancos", "saldo_actual", saldoEnviado, "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo().toString() + "'");
                
                
                final Inventory inventoryIncome = new Inventory(
                    id_inventario, 
                    cycle.getDate(), 
                    "INGRESO", 
                    cycle.getQuantityReceived(), 
                    cycle.getCurrencyReceived(), 
                    cycle.getReceivedMethod(), 
                    "CICLO", 
                    cycle.getTime(), 
                    cycle.getId()
                    );
                
                id_inventario = ULID.generate(System.currentTimeMillis(), random);

                final Inventory inventoryExpense = new Inventory(
                    id_inventario, 
                    cycle.getDate(), 
                    "EGRESO", 
                    cantidadEnviada, 
                    cycle.getSentCurrency(), 
                    cycle.getSentMethod(), 
                    tipoOperacion, 
                    cycle.getTime(), 
                    cycle.getId()
                    );
               
                inventoryDAO.newRegister(inventoryIncome);
                inventoryDAO.newRegister(inventoryExpense);
               
                Alert alert = new Alert(AlertType.INFORMATION, "Ciclo creado con exito", ButtonType.CLOSE);
                alert.showAndWait();

                clearfields();
                Main.switchToDashboard();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING, "Debe llenar todos los campos para crear el ciclo");
            alert.showAndWait();
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
