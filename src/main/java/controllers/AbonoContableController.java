package main.java.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.dao.CicleDAO;
import main.java.dao.CxCDAO;
import main.java.dao.ExpensesDAO;
import main.java.dao.InventoryDAO;
import main.java.entities.Accounts;
import main.java.entities.Banks;
import main.java.entities.CxC;
import main.java.entities.Cycle;
import main.java.entities.Gastos;
import main.java.util.DatabaseUtils;
import main.java.util.SceneSwitcher;
import main.java.util.TimeZone;
import main.java.util.ULID;
import java.sql.Timestamp;

public class AbonoContableController {
    @FXML
    private Pane AnchorPane;
    @FXML
    private TextField montoId; //cambiar a montoFieldId;
    @FXML 
    private ComboBox<Banks> metodoId; //cambiar a banksComboBox;
    @FXML
    private Label infoTransactionId; //informationDataLabel;
    @FXML
    private Label infoTransactionId1; //informationPendingLabel;
    @FXML
    private Label montoMaxId; //
    @FXML
    private Label bankInfoId; //bankInformationLabel;
    @FXML
    private CheckBox checkPMId; //
    @FXML
    private TextField porcentajePM;
    @FXML
    private Label comisionPM;
    @FXML
    private Button abonarButton;
    @FXML
    private Button cancelarButton;


    public void initialize () throws SQLException {
       
        /*
         * Inicializar informacion sobre cuenta contable
         */
        
        montoMaxId.setText("El abono debe ser menor o igual a " + cuentaXAbonar.getPendienteTransaccion());
        infoTransactionId.setText(
            "Cuenta #" + 
            cuentaXAbonar.getIdTransaction() + 
            ". Cliente: " + cuentaXAbonar.getCliente() + 
            ". ");
        infoTransactionId1.setText(
            "Monto total: " +
            cuentaXAbonar.getMontoTransaccion() + 
            " " + cuentaXAbonar.getMonedaTransaccion() + 
            ". Total abonado: " + cuentaXAbonar.getAbonadoTransaccion() +
            " " + cuentaXAbonar.getMonedaTransaccion() + 
            ". Pendiente: " + cuentaXAbonar.getPendienteTransaccion() +
            " " + cuentaXAbonar.getMonedaTransaccion()
        );

        /*
         * Inicializar bancos donde enviar / recibir el abono. 
         */

        banksDAO = new BanksDAO();
        dbUtils = new DatabaseUtils();

        typeMoney = cuentaXAbonar.getMonedaTransaccion();

        List<Banks> listaBancos = new ArrayList<>();

        if (typeMoney.equals("USD") || typeMoney.equals("USDT"))
            banksDAO.setBank(listaBancos, "moneda", "USD' or moneda = 'USDT");
        else
            banksDAO.setBank(listaBancos, "moneda", typeMoney);

        metodoId.getItems().addAll(listaBancos);

        crearComponentes();

        /*
         * Acciones de Check Box y ComboBox
         */

         if (typeMoney.equals("VES")) {
            checkPMId.setVisible(true);
            abonarButton.setLayoutY(290);
            cancelarButton.setLayoutY(290);
         }
         else {
            abonarButton.setLayoutY(290);
            cancelarButton.setLayoutY(290);
         }

        checkPMId.setOnAction(_ -> {
            if (checkPMId.isSelected()) {
                porcentajePM.setVisible(true);
                porcentajePM.setDisable(false);
                abonarButton.setLayoutY(340);
                cancelarButton.setLayoutY(340);
            }
            else {
                porcentajePM.setVisible(false);
                porcentajePM.setDisable(true);
                porcentajePM.setText("");
                abonarButton.setLayoutY(290);
                cancelarButton.setLayoutY(290);
            }
        });

        metodoId.setOnAction(_ -> {
            try {
                bankInfoId.setText(
                    metodoId.getSelectionModel().getSelectedItem().getCodigo() + 
                    " | " + 
                    metodoId.getSelectionModel().getSelectedItem().getNombre());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        /*
         * Listeners de TextFields
         */
        porcentajePM.textProperty().addListener((_, _, newValue) -> {
            if(!porcentajePM.getText().isEmpty() && !montoId.getText().isEmpty()) {
                Double porcentaje = 0.0;
                porcentaje = Double.parseDouble(newValue)/100;
                porcentaje = porcentaje * Double.parseDouble(montoId.getText());
                comisionAbono = porcentaje;
                comisionPM.setText(porcentaje.toString() + " VES");
            }
            else 
            comisionPM.setText("");
        });

        montoId.textProperty().addListener((_, _, newValue) -> {
            if (!porcentajePM.getText().isEmpty() && !montoId.getText().isEmpty()) {
                Double porcentaje = (Double.parseDouble(porcentajePM.getText()))/100;
                porcentaje = porcentaje * Double.parseDouble(newValue);
                comisionAbono = porcentaje;
                comisionPM.setText(porcentaje.toString() + " VES");
            }
            else 
            comisionPM.setText("");
        });

    }

    public static void setCuenta (CxC cuentaAbonar) {
        cuentaXAbonar = cuentaAbonar;
    }

    public void abonarCuenta () throws SQLException {
        if (!montoId.getText().isEmpty() && !metodoId.getSelectionModel().isEmpty()) {
            Double monto = Double.parseDouble(montoId.getText());
        if (monto <= cuentaXAbonar.getPendienteTransaccion()) {
            inventoryDAO = new InventoryDAO();
            byte[] random = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
            
            String id_inventario, tipoMovimiento, tipoMetodo, tipoMoneda;
            
            Double balanceNuevo = 0.0, saldoBanco = metodoId.getSelectionModel().getSelectedItem().getSaldo();

            if ((cuentaXAbonar.getTipoCuenta().equals("CUENTASXPAGAR") || cuentaXAbonar.getTipoCuenta().equals("CICLOSXPAGAR")) 
            && (saldoBanco <= 0 || saldoBanco < monto)) {
                Alert alert = new Alert(AlertType.ERROR, "No hay saldo suficiente en esta cuenta");
                alert.showAndWait();
            }
            else {
                String tabla = "transacciones";
                if (cuentaXAbonar.getTipoCuenta().equals("CUENTASXCOBRAR")) {
                    id_inventario = ULID.generate(System.currentTimeMillis(), random);
                    tipoMovimiento = "INGRESO";
                    tipoMetodo = "metodo_recibido";
                    tipoMoneda = "moneda_recibida";
                    balanceNuevo = saldoBanco + monto;
                }
                else {
                    id_inventario = ULID.generate(System.currentTimeMillis(), random);
                    tipoMovimiento = "EGRESO";
                    tipoMetodo = "metodo_enviado";
                    tipoMoneda = "moneda_enviada";
                    balanceNuevo = saldoBanco - monto;
                    if (cuentaXAbonar.getTipoCuenta().equals("CICLOSXPAGAR"))
                        tabla = "ciclos";
                }


                Date fecha = Date.valueOf(LocalDate.parse(TimeZone.getDateZoneCaracas()));
                LocalTime hora = LocalTime.parse(TimeZone.getTimeZoneCaracas());
                LocalDateTime fechaHora = LocalDateTime.of(LocalDate.parse(TimeZone.getDateZoneCaracas()), hora);
                Timestamp timestamp = Timestamp.valueOf(fechaHora);
                

                if (typeMoney.equals("VES") && tipoMovimiento.equals("EGRESO") && !porcentajePM.getText().isEmpty()) {
                    balanceNuevo = balanceNuevo - comisionAbono; //El balance nuevo del banco es el balance actual - esa comision del pago movil
                    String id_inventarioComision = "E-" + ULID.generate(System.currentTimeMillis(), random);
                    String id_gasto = ULID.generate(System.currentTimeMillis(), random);
                    
                    inventoryDAO.newRegister(id_inventarioComision, fecha, timestamp, tipoMovimiento, comisionAbono, metodoId.getSelectionModel().getSelectedItem().getMoneda(), 
                    metodoId.getSelectionModel().getSelectedItem().getCodigo(), "GASTO", cuentaXAbonar.getIdTransaction());
                   
                    String lastCycleId = dbUtils.getInfoByLastReferenceOf("ciclos", "id", null, null);
                    CicleDAO cicleDAO = new CicleDAO();
                    Cycle lastCycle = cicleDAO.getCycle(lastCycleId);

                    Gastos gasto = new Gastos(
                        id_gasto, lastCycle,
                        Main.getUsername(), fecha, 
                        "Operaciones", Accounts.PAGO_MOVIL, 
                        cuentaXAbonar.getCliente(), 
                        "Comision por Pago Movil", 
                        comisionAbono, 
                        metodoId.getSelectionModel().getSelectedItem(), 
                        comisionAbono);
                    
                    ExpensesDAO expensesDAO = new ExpensesDAO();
                    expensesDAO.registerExpense(gasto);

                    //Y de paso Guardar en gastos como comisión x pago móvil
                    dbUtils.updateRegister("transacciones", "ganancia", "ganancia - " + gasto.getUsd_Equivalente(), "id = '" + cuentaXAbonar.getIdTransaction() + "'");

                }

                inventoryDAO.newRegister(id_inventario, fecha, timestamp, tipoMovimiento, monto, metodoId.getSelectionModel().getSelectedItem().getMoneda(), 
                metodoId.getSelectionModel().getSelectedItem().getCodigo(), "ABONO", cuentaXAbonar.getIdTransaction());
                
                dbUtils.updateRegister(tabla, tipoMetodo, metodoId.getSelectionModel().getSelectedItem().getCodigo(), "id = '" + cuentaXAbonar.getIdTransaction() + "'");
                
                if (typeMoney != metodoId.getSelectionModel().getSelectedItem().getMoneda())
                    dbUtils.updateRegister(tabla, tipoMoneda, metodoId.getSelectionModel().getSelectedItem().getMoneda(), "id = '" + cuentaXAbonar.getIdTransaction() + "'");

                dbUtils.updateRegister("bancos", "saldo_actual", balanceNuevo, "codigo = '" + metodoId.getSelectionModel().getSelectedItem().getCodigo() + "'");
                CxCDAO cxcDAO = new CxCDAO();
                cxcDAO.actualizarCxC(cuentaXAbonar);
                
                if (cuentaXAbonar.getPendienteTransaccion() == 0) {
                    dbUtils.updateRegister(tabla, "status", "OK", "id = '" + cuentaXAbonar.getIdTransaction() + "'");
                    Alert alert = new Alert(AlertType.INFORMATION, "Cuenta por el monto " + monto.toString() + " " + cuentaXAbonar.getMonedaTransaccion() + " saldada completamente! ");
                    alert.showAndWait();
                }
                else {
                    System.out.println("Abonado. ");
                }

            }  
            
            backButton();
        }
        else {
            Alert alert = new Alert(AlertType.ERROR, "Parece que ha introducido un dato erroneo. Intente nuevamente");
            alert.showAndWait();
        }
        }
        else {
            Alert alert = new Alert(AlertType.ERROR, "Debe llenar los campos para el abono");
            alert.showAndWait();
        }
    }

    public void backButton() {
        try {
            if (cuentaXAbonar.getTipoCuenta() == "CUENTASXCOBRAR")
            SceneSwitcher.switchPane(AnchorPane, "/main/resources/fxml/cuentasPorCobrar.fxml", "/main/resources/css/cxc.css", new CuentasPorCobrarController());
        else if (cuentaXAbonar.getTipoCuenta() == "CUENTASXPAGAR" || cuentaXAbonar.getTipoCuenta() == "CICLOSXPAGAR")
            SceneSwitcher.switchPane(AnchorPane, "/main/resources/fxml/cuentasPorPagar.fxml", "/main/resources/css/cxc.css", new CuentasPorPagarController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearComponentes () {
        metodoId.setButtonCell(new ListCell<Banks>() {
            @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                        if (banco != null) {
                            setText(banco.getCodigo());
                        }
                        else 
                            setText(null);
                }
        });

        metodoId.setCellFactory((ListView<Banks> _) -> {
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

    private BanksDAO banksDAO;
    private String typeMoney;
    private static CxC cuentaXAbonar;
    private InventoryDAO inventoryDAO;
    private DatabaseUtils dbUtils;
    private Double comisionAbono;
}
