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

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import main.java.dao.BanksDAO;
import main.java.dao.InventoryDAO;
import main.java.entities.Banks;
import main.java.util.ConnectionPool;
import main.java.util.DatabaseUtils;
import main.java.util.TimeZone;
import main.java.util.ULID;

public class RegisterInversionController {
    @FXML
    private ComboBox<Banks> bancosBox;
    @FXML
    private Label saldoDisponibleLabel;
    @FXML
    private TextField montoInversion;
    @FXML
    private Label nameBankId;

    public DatabaseUtils dbUtils;
    public InventoryDAO inventoryDAO;

    public void initialize () throws SQLException {

        List<Banks> listaBanks = new ArrayList<>();

        BanksDAO banksDAO = new BanksDAO();
        banksDAO.setBank(listaBanks, null, null);

        bancosBox.getItems().addAll(listaBanks);
        crearComponentes();

        bancosBox.setOnAction(e -> {
            try {
                saldoDisponibleLabel.setText(
                    "Saldo: " + bancosBox.getSelectionModel().getSelectedItem().getSaldo() + 
                    " " + bancosBox.getSelectionModel().getSelectedItem().getMoneda());
                nameBankId.setText(bancosBox.getSelectionModel().getSelectedItem().getNombre());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        formatosTextField();

    }

    public void formatosTextField () {
        UnaryOperator <TextFormatter.Change> filterMonto = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*\\d*(\\.\\d{0,3})?")) {
                return change;
            }

            return null;
        };

        TextFormatter<String> formatoMonto = new TextFormatter<>(filterMonto);
        montoInversion.setTextFormatter(formatoMonto);

    }

    public void registerInversion () throws SQLException {

        if (montoInversion.getText().isEmpty() || bancosBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING, "Error. Debe llenar todos los campos para el registro");
            alert.showAndWait();
        }
        else {
            Double monto = Double.parseDouble(montoInversion.getText());
            Date fecha = Date.valueOf(LocalDate.parse(TimeZone.getDateZoneCaracas()));
            LocalTime hora = LocalTime.parse(TimeZone.getTimeZoneCaracas());
            LocalDateTime fechaHora = LocalDateTime.of(LocalDate.parse(TimeZone.getDateZoneCaracas()), hora);
            Timestamp timestamp = Timestamp.valueOf(fechaHora);

            byte[] random = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
            String idInversion = "I-" + ULID.generate(System.currentTimeMillis(), random);
            
            inventoryDAO = new InventoryDAO(ConnectionPool.getConnection());
            inventoryDAO.newRegister(idInversion, fecha, timestamp, "INGRESO", monto, bancosBox.getSelectionModel().getSelectedItem().getMoneda(), 
            bancosBox.getSelectionModel().getSelectedItem().getCodigo(), "INVERSION", idInversion);
            dbUtils.updateRegister("bancos", "saldo_actual", bancosBox.getSelectionModel().getSelectedItem().getSaldo() + monto, "codigo = '" + bancosBox.getSelectionModel().getSelectedItem().getCodigo() + "'");
            }
        
            montoInversion.clear();
            nameBankId.setText("");
            saldoDisponibleLabel.setText("");
    }
    public void crearComponentes () {
        bancosBox.setButtonCell(new ListCell<Banks>() {
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

        bancosBox.setCellFactory((ListView<Banks> e) -> {
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

}
