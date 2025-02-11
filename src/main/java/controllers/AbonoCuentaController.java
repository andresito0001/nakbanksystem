package main.java.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.java.dao.BanksDAO;
import main.java.dao.InventoryDAO;
import main.java.entities.CxC;
import main.java.util.ConnectionPool;
import main.java.util.DatabaseUtils;
import main.java.util.SceneSwitcher;
import main.java.util.ULID;


public class AbonoCuentaController {
    @FXML
    private TextField montoId;
    @FXML
    private ComboBox<String> metodoId;
    @FXML
    private Label infoTransactionId;
    @FXML
    private Button abonarButton;
    @FXML
    private Button cancelarButton;
    @FXML
    private Label bankInfoId;
    @FXML
    private Label infoTransactionId1;
    @FXML
    private Label montoMaxId;
    @FXML
    private CheckBox checkPMId;
    @FXML
    private TextField porcentajePM;
    @FXML
    private Label comisionPM;

    private String tipoCuenta, comision;


    private static CxC cuentaXAbonar;

    private InventoryDAO inventoryDAO;
        
        public void initialize () throws SQLException {

        String typeMoneyReceived = cuentaXAbonar.getMonedaTransaccion(); 
        List<String> bankCodes = new ArrayList<>();
        bankCodes = new BanksDAO(ConnectionPool.getConnection()).getInfoOf("codigo","moneda", typeMoneyReceived); //query = "select " + colum + " from bancos where " + key + " = " + "'" + USD' OR MONEDA = 'USDT + "'" + ";";
        if (typeMoneyReceived.equals("USD") || typeMoneyReceived.equals("USDT"))
            {
                bankCodes = new BanksDAO(ConnectionPool.getConnection()).getInfoOf("codigo", "moneda", "USD' or moneda = 'USDT");
            }
        metodoId.getItems().addAll(bankCodes); 
       
        montoMaxId.setText("Monto debe ser menor o igual a " + cuentaXAbonar.getPendienteTransaccion());
        infoTransactionId.setText("Cuenta #" + cuentaXAbonar.getIdTransaction() + ". Cliente: " + cuentaXAbonar.getCliente() + ". ");       
        infoTransactionId1.setText("Monto Total: " + cuentaXAbonar.getMontoTransaccion() + " " + cuentaXAbonar.getMonedaTransaccion() + ". Total Abonado: " + cuentaXAbonar.getAbonadoTransaccion() + ". Pendiente: " + cuentaXAbonar.getPendienteTransaccion());
        if (typeMoneyReceived.equals("VES")) {
            checkPMId.setVisible(true);
            abonarButton.setLayoutY(343);
            cancelarButton.setLayoutY(343);
        }
        checkPMId.setOnAction(_ -> {
            if (checkPMId.isSelected()) {
                porcentajePM.setVisible(true);
                porcentajePM.setDisable(false);
            } else {
                porcentajePM.setVisible(false);
                porcentajePM.setDisable(true);
                porcentajePM.setText("");
            }
        });



        porcentajePM.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!porcentajePM.getText().isEmpty() && !montoId.getText().isEmpty()) {
                Double porcentaje = 0.0;
                porcentaje = Double.parseDouble(newValue)/100;
                porcentaje = porcentaje * Double.parseDouble(montoId.getText());
                comisionPM.setText(porcentaje.toString() + " VES");
            }
            else 
            comisionPM.setText("");
        });
        
        montoId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!porcentajePM.getText().isEmpty() && !montoId.getText().isEmpty()) {
                Double porcentaje = (Double.parseDouble(porcentajePM.getText()))/100;
                porcentaje = porcentaje * Double.parseDouble(newValue);
                comisionPM.setText(porcentaje.toString() + " VES");
            }
            else 
            comisionPM.setText("");
        });
        
        metodoId.setOnAction(e -> {
            try {
                String codeBank = metodoId.getSelectionModel().getSelectedItem();
                DatabaseUtils dbUtils = new DatabaseUtils(ConnectionPool.getConnection());
                String nameBank = dbUtils.getValueOf("nombre_banco", "bancos", "codigo = '" + codeBank + "'").toString();
                
                bankInfoId.setText(codeBank + " " + nameBank);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
    }

    public static void setCuenta(CxC cuentaXCobrar) {
        cuentaXAbonar = cuentaXCobrar;
    }

    public void abonarCuenta () throws SQLException {
        tipoCuenta = cuentaXAbonar.getTipoCuenta();
        Double monto = Double.parseDouble(montoId.getText());
        if(monto <= cuentaXAbonar.getPendienteTransaccion())
        {
            System.out.println(tipoCuenta);
            inventoryDAO = new InventoryDAO(ConnectionPool.getConnection());
            DatabaseUtils dbUtils = new DatabaseUtils(ConnectionPool.getConnection());
            
            try (PreparedStatement st = ConnectionPool.getConnection().prepareStatement("select now () as hoy")) {
                ResultSet rs = st.executeQuery();

                byte[] random = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
                final String id_inventario = ULID.generate(System.currentTimeMillis(), random);
                
                String idAbono = "", tipoMovimiento = "", tipoMetodo = "", tipoMoneda = "";
                BanksDAO banksDAO = new BanksDAO(ConnectionPool.getConnection());
                final Double totalBalance = banksDAO.getTotalBalanceOf(metodoId.getSelectionModel().getSelectedItem());
                final String moneda = dbUtils.getValueOf("moneda", "bancos", "codigo = '" + metodoId.getSelectionModel().getSelectedItem() + "'").toString();
                Double balanceNuevo = 0.0;
                
                if (tipoCuenta == "CUENTASXCOBRAR") {
                    idAbono = "I-" + id_inventario;
                    tipoMovimiento = "INGRESO";
                    tipoMetodo = "metodo_recibido";
                    tipoMoneda = "moneda_recibida";
                    balanceNuevo = totalBalance + monto;

                }
                else if (tipoCuenta == "CUENTASXPAGAR") { 
                    idAbono = "E-" + id_inventario;
                    tipoMovimiento = "EGRESO";
                    tipoMetodo = "metodo_enviado";
                    tipoMoneda = "moneda_enviada";
                    balanceNuevo = totalBalance - monto;
                }

                if (totalBalance <= 0 && tipoCuenta == "CUENTASXPAGAR"|| totalBalance < monto && tipoCuenta == "CUENTASXPAGAR")
                {
                    Alert alert = new Alert(AlertType.WARNING, "ERROR. No hay saldo suficiente en " + metodoId.getSelectionModel().getSelectedItem());
                    alert.showAndWait();
                }
                else {
                    while (rs.next()) {
                        inventoryDAO.newRegister(idAbono, rs.getDate("hoy"), rs.getTimestamp("hoy"), tipoMovimiento, monto, cuentaXAbonar.getMonedaTransaccion(), metodoId.getSelectionModel().getSelectedItem(), "ABONO", cuentaXAbonar.getIdTransaction());
                        dbUtils.updateRegister("trans", tipoMetodo, metodoId.getSelectionModel().getSelectedItem(), "id = '" + cuentaXAbonar.getIdTransaction() + "'");
                        dbUtils.updateRegister("trans",tipoMoneda, moneda, "id = '" + cuentaXAbonar.getIdTransaction() + "'");
                        cuentaXAbonar.actualizarPendiente(ConnectionPool.getConnection(), tipoCuenta);
                    }
    
                    dbUtils.updateRegister("bancos", "saldo_actual", balanceNuevo, "codigo = '" + metodoId.getSelectionModel().getSelectedItem() + "'");
    
                    if (cuentaXAbonar.getPendienteTransaccion() == 0) {
                        dbUtils.updateRegister("trans", "status", "OK", "id = '" + cuentaXAbonar.getIdTransaction() + "'");
                        Alert alert = new Alert(AlertType.INFORMATION, "Cuenta por el monto " + monto.toString() + " " + cuentaXAbonar.getMonedaTransaccion() + " saldada completamente! ");
                        alert.showAndWait();
                    }
                    else {
                        System.out.println("Le queda un pendiente de " + cuentaXAbonar.getPendienteTransaccion());
                    }
                }

                
                rs.close();
                st.close();
            }
        }
        else {
            Alert alert = new Alert(AlertType.INFORMATION, "Error en: " + monto.toString());
            alert.showAndWait();
        }

        try {
            if (tipoCuenta == "CUENTASXCOBRAR")
                SceneSwitcher.switchPane(AnchorPane, "/main/resources/fxml/cuentasPorCobrar.fxml", "/main/resources/css/cxc.css", new CuentasPorCobrarController());
            else if (tipoCuenta == "CUENTASXPAGAR")
                SceneSwitcher.switchPane(AnchorPane, "/main/resources/fxml/cuentasPorPagar.fxml", "/main/resources/css/cxc.css", new CuentasPorPagarController());
            } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void backButton(MouseEvent event) {
        try {
            if (tipoCuenta == "CUENTASXCOBRAR")
            SceneSwitcher.switchPane(AnchorPane, "/main/resources/fxml/cuentasPorCobrar.fxml", "/main/resources/css/cxc.css", new CuentasPorCobrarController());
        else if (tipoCuenta == "CUENTASXPAGAR")
            SceneSwitcher.switchPane(AnchorPane, "/main/resources/fxml/cuentasPorPagar.fxml", "/main/resources/css/cxc.css", new CuentasPorPagarController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private Pane AnchorPane;
    
}
