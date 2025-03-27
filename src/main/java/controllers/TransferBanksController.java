package main.java.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.dao.InventoryDAO;
import main.java.entities.Banks;
import main.java.entities.Inventory;
import main.java.util.DatabaseUtils;
import main.java.util.ULID;

public class TransferBanksController {
    @FXML
    private ComboBox<Banks> sentComboBox;
    @FXML
    private ComboBox<Banks> receivedComboBox;
    @FXML
    private CheckBox PMCheck;
    @FXML
    private TextField pmId;
    @FXML
    private TextField sentTextField;
    @FXML
    private TextField totalId;
    @FXML
    private TextField refTextField;

    public void initialize () throws SQLException {

        List<Banks> listaEnviados = new ArrayList<>();
        List<Banks> listaRecibidos = new ArrayList<>();

        BanksDAO banksDAO = new BanksDAO();

        banksDAO.setBank(listaEnviados, null, null);
        sentComboBox.getItems().addAll(listaEnviados);

        sentComboBox.setOnAction(_ -> { 
            listaRecibidos.clear();
            receivedComboBox.getItems().clear();
            receivedComboBox.getSelectionModel().clearSelection();
            
            banksDAO.setBank(listaRecibidos, "moneda", sentComboBox.getSelectionModel().getSelectedItem().getMoneda());
            receivedComboBox.getItems().addAll(listaRecibidos);

            if(!sentComboBox.getSelectionModel().getSelectedItem().equals(null) && sentComboBox.getSelectionModel().getSelectedItem().getMoneda().equals("VES")) {
                PMCheck.setDisable(false);
            } else {
                PMCheck.setDisable(true);
            }
        });

        sentTextField.textProperty().addListener((_,_, _) -> {
            if (!sentTextField.getText().isEmpty() && !receivedComboBox.getSelectionModel().isEmpty()) {
                Double totalSent = Double.parseDouble(sentTextField.getText().toString());
                if (!pmId.getText().isEmpty()) {
                    Double porcentaje = Double.parseDouble(pmId.getText().toString()) / 100;
                    porcentaje = porcentaje * totalSent;
                    totalSent = totalSent + porcentaje;
                }
                totalId.setText(totalSent.toString());
            } else {
                totalId.setText("");
            }
        });

        
        PMCheck.setOnAction(_ -> {
            if (PMCheck.isSelected()) {
                pmId.setDisable(false);
            } else {
                pmId.setDisable(true);
            }
        });

        crearComponentes();
        formatos();


    }

    public void crearComponentes () {
        sentComboBox.setButtonCell(new ListCell<Banks>() {
            @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                        if(banco != null) {
                            setText(banco.getNombre());
                        }
                        else
                            setText(null);
                }
        });
        receivedComboBox.setButtonCell(new ListCell<Banks>() {
            @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                        if(banco != null) {
                            setText(banco.getNombre());
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
                        setText(banco.getCodigo());
                     }
                     else 
                        setText(null);
                }
            };
            return listCell;
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
    }

    public void formatos () {
            UnaryOperator <TextFormatter.Change> filterMonto = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*\\d*(\\.\\d{0,3})?")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> formatoMonto = new TextFormatter<>(filterMonto);
        TextFormatter<String> formatoPM = new TextFormatter<>(filterMonto);

        sentTextField.setTextFormatter(formatoMonto);
        pmId.setTextFormatter(formatoPM);

    }
    
    public void transfer () throws SQLException {
        if (!receivedComboBox.getSelectionModel().isEmpty() && !sentComboBox.getSelectionModel().isEmpty() && !totalId.getText().isEmpty()) {
            if (Double.parseDouble(totalId.getText().toString()) <= receivedComboBox.getSelectionModel().getSelectedItem().getSaldo()) {

                DatabaseUtils dbUtils = new DatabaseUtils();
                Double sent = Double.parseDouble(totalId.getText().toString());
    
                final byte[] entropy = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
    
                final String date = main.java.util.TimeZone.getDateZoneCaracas();
                final String time = main.java.util.TimeZone.getTimeZoneCaracas();
    
                String id_inventario = ULID.generate(System.currentTimeMillis(), entropy);
                Inventory ingreso = new Inventory (
                    id_inventario,
                    date, 
                    "INGRESO", 
                    sent, 
                    sentComboBox.getSelectionModel().getSelectedItem().getMoneda(), 
                    sentComboBox.getSelectionModel().getSelectedItem().getCodigo(), 
                    "TRASPASO", 
                    time, 
                    id_inventario
                    );
    
                InventoryDAO inventoryDAO = new InventoryDAO();
                inventoryDAO.newRegister(ingreso);
                
                dbUtils.updateRegister("bancos", "saldo_actual", sentComboBox.getSelectionModel().getSelectedItem().getSaldo() + ingreso.getQuantity(), "codigo = '" + receivedComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
                
                id_inventario = ULID.generate(System.currentTimeMillis(), entropy);
                
                Inventory egreso = new Inventory(
                    id_inventario, 
                    date, 
                    "EGRESO", 
                    sent, 
                    receivedComboBox.getSelectionModel().getSelectedItem().getMoneda(),
                    receivedComboBox.getSelectionModel().getSelectedItem().getCodigo(),
                    "TRASPASO", 
                    time, 
                    id_inventario);
    
                    dbUtils.updateRegister("bancos", "saldo_actual", sentComboBox.getSelectionModel().getSelectedItem().getSaldo() - egreso.getQuantity(), "codigo = '" + sentComboBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
    
                    inventoryDAO.newRegister(egreso);
    
                    Alert alert = new Alert(AlertType.INFORMATION, "Transferencia realizada exitosamente");
                    alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.ERROR, "Saldo insuficiente");
                alert.showAndWait();
            }


        } else {
            Alert alert = new Alert(AlertType.WARNING, "Debe llenar todos los campos");
            alert.showAndWait();
        }


    }

    public void clearfields () {
        Main.switchToDashboard();       
    }
}
