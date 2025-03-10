package main.java.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.dao.CicleDAO;
import main.java.dao.ExpensesDAO;
import main.java.dao.InventoryDAO;
import main.java.entities.Accounts;
import main.java.entities.Banks;
import main.java.entities.Cycle;
// import main.java.entities.Gastos;
import main.java.entities.Gastos;
import main.java.entities.Inventory;
import main.java.util.DatabaseUtils;
import main.java.util.TimeZone;
import main.java.util.ULID;

public class RegisterExpenseController {
    @FXML 
    private ComboBox<Banks> metodoPagoId;
    @FXML
    private MenuButton departamentoMenuId;
    @FXML
    private TextField tipoGastoId;
    @FXML
    private TextField montoGastoId;
    @FXML
    private TextField equivalenteId;
    @FXML
    private TextField proveedorId;
    @FXML
    private TextField descripcionId;
    @FXML
    private DatePicker fechaGastoId;

    String departamento;
    Accounts cuentaSeleccionada;
    Cycle lastCycle;
    DatabaseUtils databaseUtils;

    public void initialize() throws SQLException{

        List<Banks> listaBanks = new ArrayList<>();
        BanksDAO banksDAO = new BanksDAO();
        databaseUtils = new DatabaseUtils();

        banksDAO.setBank(listaBanks, null, null);

        metodoPagoId.getItems().addAll(listaBanks);

        crearComponentes();
        
        menuGerenciaGeneral = new Menu("Gerencia General");
        menuRecursosHumanos = new Menu("Recursos Humanos");
        menuOperaciones = new Menu("Operaciones");
        menuTecnologia = new Menu("Tecnologia");
        menuFinanzas = new Menu("Finanzas");

        menuGerenciaGeneral.setOnShowing(event -> {
            departamento = "Gerencia General";
        });

        menuRecursosHumanos.setOnShowing(event -> {
            departamento = "Recursos Humanos";
        });

        menuFinanzas.setOnShowing(event -> {
            departamento = "Finanzas";
        });
        
        menuOperaciones.setOnShowing(event -> {
            departamento = "Operaciones";
        });

        menuTecnologia.setOnShowing(event -> {
            departamento = "Tecnologia";
        });
        
        departamentoMenuId.getItems().addAll(menuGerenciaGeneral,menuOperaciones,menuRecursosHumanos,menuTecnologia, menuFinanzas);

        menuGerenciaGeneral.getItems().addAll(
            crearMenu("Operativos", menuGerenciaGeneral),
            crearMenu("Generales", menuGerenciaGeneral),
            crearMenu("Eventos", menuGerenciaGeneral),
            crearMenu("Personal Local", menuGerenciaGeneral)
        );
        menuRecursosHumanos.getItems().addAll(
            crearMenu("Operativos", menuRecursosHumanos),
            crearMenu("Generales", menuRecursosHumanos),
            crearMenu("Eventos", menuRecursosHumanos),
            crearMenu("Personal Local", menuRecursosHumanos)
        );
        menuOperaciones.getItems().addAll(
            crearMenu("Operativos", menuOperaciones),
            crearMenu("Generales", menuOperaciones),
            crearMenu("Eventos", menuOperaciones),
            crearMenu("Personal Local", menuOperaciones)
        );
        menuTecnologia.getItems().addAll(
            crearMenu("Operativos", menuTecnologia),
            crearMenu("Generales", menuTecnologia),
            crearMenu("Eventos", menuTecnologia),
            crearMenu("Personal Local", menuTecnologia)
        );
        menuFinanzas.getItems().addAll(
            crearMenu("Operativos", menuFinanzas),
            crearMenu("Generales", menuFinanzas),
            crearMenu("Eventos", menuFinanzas),
            crearMenu("Personal Local", menuFinanzas),
            crearMenu("Prestamos", menuFinanzas)
        );

        metodoPagoId.setOnAction(_ -> { 
            if (!montoGastoId.getText().isEmpty()) {
                montoGastoId.setText("");
            }
        });

        montoGastoId.textProperty().addListener((_, _, _) -> {
            if (!metodoPagoId.getSelectionModel().isEmpty() && !montoGastoId.getText().isEmpty()) {
                Double montoEquivalente = Double.parseDouble(montoGastoId.getText().toString());
                if (metodoPagoId.getSelectionModel().getSelectedItem().getMoneda().equals("VES")) {
                    montoEquivalente = montoEquivalente / 70;
                    BigDecimal monto = new BigDecimal(montoEquivalente);
                    monto = monto.setScale(3, RoundingMode.HALF_UP);
                    equivalenteId.setText(monto.toString() + " USD");
                }
                else
                    equivalenteId.setText(montoEquivalente.toString() + " USD");
            }
            else 
                equivalenteId.setText("");
        });

        fechaGastoId.valueProperty().addListener((observable, old, newValue) -> {
            if (!(fechaGastoId.getValue() == null)) {
                try {
                    String id_ciclo = databaseUtils.getValueOf("id", "ciclos", "fecha <= '" + newValue + "' order by fecha desc limit 1; ").toString();
                    CicleDAO cicleDAO = new CicleDAO();
                    lastCycle = cicleDAO.getCycle(id_ciclo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

        UnaryOperator <TextFormatter.Change> filterProveedor = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*")) {
                return change;
            }
            return null;
        };

        UnaryOperator <TextFormatter.Change> filterEquivalente = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*(\\.\\d{0,3})?\\s[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*")) {
                return change;
            }
            else if (newText.matches(""))
                return change;
            return null;
        };

        TextFormatter<String> formatoMonto = new TextFormatter<>(filterMonto);
        TextFormatter<String> formatoProveedor = new TextFormatter<>(filterProveedor);
        TextFormatter<String> formatoEquivalente = new TextFormatter<>(filterEquivalente);

        montoGastoId.setTextFormatter(formatoMonto);
        proveedorId.setTextFormatter(formatoProveedor);      
        equivalenteId.setTextFormatter(formatoEquivalente);  
    }

    public void crearComponentes() {
        metodoPagoId.setButtonCell(new ListCell<Banks>() {
            @Override
                public void updateItem(Banks banco, boolean empty) {
                    super.updateItem(banco, empty);
                        if(banco != null) {
                            setText(banco.getNombre());
                            if (!montoGastoId.getText().isEmpty()) {
                                Double montoEquivalente = Double.parseDouble(montoGastoId.getText().toString());
                                    if (metodoPagoId.getSelectionModel().getSelectedItem().getMoneda().equals("VES") && !(fechaGastoId.getValue() == null)) {
                                        montoEquivalente = montoEquivalente / lastCycle.getRate(); //debería calcular en base al promedio del ciclo activo 
                                    }
                                equivalenteId.setText(montoEquivalente.toString() + " USD");
                            }
                        }
                        else
                            setText(null);
                }
        });

            metodoPagoId.setCellFactory((ListView<Banks> _) -> {
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
    
    private Menu crearMenu(String nombre, Menu menuPrincipal) {
        Menu menu = new Menu(nombre);
            switch (nombre) {
                case "Operativos":
                    if(menuPrincipal.equals(menuGerenciaGeneral)) {
                        menu.getItems().addAll(
                            crearItem(Accounts.GASTOSVARIOS),
                            crearItem(Accounts.ARTICULOS_OFICINA),
                            crearItem(Accounts.TELECOM_SERV),
                            crearItem(Accounts.TELEPHONE)
                        );
                    } 
                    else {
                        menu.getItems().addAll(
                            crearItem(Accounts.GASTOSVARIOS),
                            crearItem(Accounts.ARTICULOS_OFICINA),
                            crearItem(Accounts.TELECOM_SERV),
                            crearItem(Accounts.TELEPHONE),
                            crearItem(Accounts.FOOD_DRINKS),
                            crearItem(Accounts.TEMP_STAFF),
                            crearItem(Accounts.SUNDRY_FEES),
                            crearItem(Accounts.PARKING_RENTAL),
                            crearItem(Accounts.VEHICLE_SERV_REP),
                            crearItem(Accounts.SOFTWARE_LICENCES)
                        );
                    }
                    break;
                case "Generales":
                    menu.getItems().addAll(
                        crearItem(Accounts.GASOLINA),
                        crearItem(Accounts.ARTICULOS_OFICINA),
                        crearItem(Accounts.HOUSEHOLD_SUPPLIES),
                        crearItem(Accounts.GAS),
                        crearItem(Accounts.ELECTRICITY),
                        crearItem(Accounts.BUILDING_RENTAL),
                        crearItem(Accounts.CATER_ACCOM_SERV),
                        crearItem(Accounts.WATER),
                        crearItem(Accounts.ACEITES_LUBRICANTES),
                        crearItem(Accounts.MEDICAL_SUPP),
                        crearItem(Accounts.ADMINISTRATIVE_TRANSPORT),
                        crearItem(Accounts.AUDIT_ACC_FEES),
                        crearItem(Accounts.DONACIONES),
                        crearItem(Accounts.OFFICE_REPAIR),
                        crearItem(Accounts.MOVING_EXPENSES)
                    );
                    
                    break;
                case "Eventos":
                    menu.getItems().addAll(
                        crearItem(Accounts.FOOD_DRINKS),
                        crearItem(Accounts.CATER_ACCOM_SERV),
                        crearItem(Accounts.TEMP_STAFF),
                        crearItem(Accounts.ADV_PROMOTIONS),
                        crearItem(Accounts.TRAVEL_EXPENSES)
                    );
                    break;

                case "Personal Local":
                    menu.getItems().addAll(
                        crearItem(Accounts.TEMP_STAFF),
                        crearItem(Accounts.MEDICAL_FEES),
                        crearItem(Accounts.GASTOSVARIOS),
                        crearItem(Accounts.SALARY),
                        crearItem(Accounts.BONUS),
                        crearItem(Accounts.VACATIONS_BONUS),
                        crearItem(Accounts.VACATIONS_PAY),
                        crearItem(Accounts.TRAINING_LENGUAJE),
                        crearItem(Accounts.TRAINING_NO_TECH),
                        crearItem(Accounts.TRAINING_TECH),
                        crearItem(Accounts.CESTATICKETS)
                    );
                    break;
                default:
                    break;
            }
        return menu;
    }
    private MenuItem crearItem(Accounts cuenta) {
        MenuItem menuItem = new MenuItem(cuenta.getCostElementName());
            menuItem.setOnAction(_ -> {
                tipoGastoId.setText(cuenta.getCostElementId());
                departamentoMenuId.setText(cuenta.getCostElementName());
                cuentaSeleccionada = cuenta;
            });

        return menuItem;
    }
    @FXML
    private void registerExpense(MouseEvent event) throws SQLException {

        if (!montoGastoId.getText().isEmpty() && !(fechaGastoId.getValue() == null) 
        && !(departamento == null) && !proveedorId.getText().isEmpty() && !(cuentaSeleccionada == null) &&
        !metodoPagoId.getSelectionModel().isEmpty() && !(lastCycle == null)) {
            byte[] random = new byte[] { 0x1, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
            String id_gasto = ULID.generate(System.currentTimeMillis(), random);
                
            Date fecha = Date.valueOf(fechaGastoId.getValue());
            Double monto = Double.parseDouble(montoGastoId.getText());

   
            Gastos gasto = new Gastos(
            id_gasto, lastCycle, Main.getUsername(), fecha, 
            departamento, cuentaSeleccionada, proveedorId.getText().toString(), 
            descripcionId.getText().toString(), Double.parseDouble(montoGastoId.getText()), metodoPagoId.getSelectionModel().getSelectedItem(), 
            monto);
           
            String id_inventario = ULID.generate(System.currentTimeMillis(), random);

            Inventory egreso = new Inventory(
                id_inventario, 
                gasto.getFecha().toString(), 
                "EGRESO", 
                gasto.getMonto(), 
                gasto.getMoneda(), 
                gasto.getMetodo(), 
                "GASTO", 
                TimeZone.getTimeZoneCaracas(), 
                id_gasto);
            
            ExpensesDAO expensesDAO = new ExpensesDAO();
            expensesDAO.registerExpense(gasto);
            
            InventoryDAO inventoryDAO = new InventoryDAO();
            inventoryDAO.newRegister(egreso);

            databaseUtils.updateRegister("bancos", "saldo_actual", (metodoPagoId.getSelectionModel().getSelectedItem().getSaldo() - gasto.getMonto()), "codigo = '" + metodoPagoId.getSelectionModel().getSelectedItem().getCodigo() + "'");

           Alert alert = new Alert(AlertType.INFORMATION,"Gasto registrado exitosamente");
            alert.showAndWait();

            clearfields();

        }
        else {
            Alert alert = new Alert(AlertType.WARNING,"Error. Debe llenar los campos obligatorios. ");
            alert.showAndWait();
        }

    }

    public void clearfields () {
        montoGastoId.clear();
        descripcionId.clear();
        proveedorId.clear();
        fechaGastoId.setValue(null);
        departamentoMenuId.setText("Departamento");
        tipoGastoId.clear();
        metodoPagoId.getSelectionModel().clearSelection();
    }
    @FXML
    Menu menuGerenciaGeneral;
    @FXML
    Menu menuRecursosHumanos;
    @FXML
    Menu menuOperaciones;
    @FXML
    Menu menuTecnologia;
    @FXML
    Menu menuFinanzas;

}
