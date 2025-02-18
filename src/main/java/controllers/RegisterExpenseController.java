package main.java.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import main.java.dao.BanksDAO;
import main.java.entities.Accounts;
import main.java.entities.Banks;
import main.java.entities.Gastos;
import main.java.util.ConnectionPool;

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

    public void initialize() throws SQLException{

        List<Banks> listaBanks = new ArrayList<>();
        BanksDAO banksDAO = new BanksDAO(ConnectionPool.getConnection());
        banksDAO.setBank(listaBanks, null, null);

        metodoPagoId.getItems().addAll(listaBanks);
        crearComponentes();
        
        menuGerenciaGeneral = new Menu("Gerencia General");
        menuRecursosHumanos = new Menu("Recursos Humanos");
        menuOperaciones = new Menu("Operaciones");
        Menu menuTecnologia = new Menu("Tecnologia");
        
        departamentoMenuId.getItems().addAll(menuGerenciaGeneral,menuOperaciones,menuRecursosHumanos,menuTecnologia);

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

        montoGastoId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!metodoPagoId.getSelectionModel().isEmpty() && !montoGastoId.getText().isEmpty()) {
                Double montoEquivalente = Double.parseDouble(montoGastoId.getText().toString());
                if (metodoPagoId.getSelectionModel().getSelectedItem().getMoneda().equals("VES")) {
                    montoEquivalente = montoEquivalente / 70;
                }
                equivalenteId.setText(montoEquivalente.toString() + " USD");
            }
            else 
                equivalenteId.setText("");
        });
    
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
                                    if (metodoPagoId.getSelectionModel().getSelectedItem().getMoneda().equals("VES")) {
                                        montoEquivalente = montoEquivalente / 70; //debería calcular en base al promedio del ciclo activo 
                                    }
                                equivalenteId.setText(montoEquivalente.toString() + " USD");
                            }
                        }
                        else
                            setText(null);
                }
        });

            metodoPagoId.setCellFactory((ListView<Banks> e) -> {
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
            menuItem.setOnAction(e -> {
                tipoGastoId.setText(cuenta.getCostElementId());
                departamentoMenuId.setText(cuenta.getCostElementName());
            });

        return menuItem;
    }
    @FXML
    private void registerExpense(MouseEvent event) {

       // Gastos gasto = new Gastos("1234", null, null, null, null, null, null, null, null, null);
        Alert alert = new Alert(AlertType.INFORMATION,"Gasto registrado exitosamente");
        alert.showAndWait();
    }
    @FXML
    Menu menuGerenciaGeneral;
    @FXML
    Menu menuRecursosHumanos;
    @FXML
    Menu menuOperaciones;

}
