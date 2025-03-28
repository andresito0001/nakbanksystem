package main.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.entities.BankInfo;
import main.java.entities.Banks;
import main.java.entities.CardModel;
import main.java.entities.TableviewTransaction;
import main.java.util.DatabaseUtils;
import main.java.util.SceneSwitcher;

public class DashBoardController implements Initializable {
    @FXML
    private void loadCycles(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(vBoxCenterPane, "/main/resources/fxml/Cycles.fxml", "/main/resources/css/Cycles.css", new CyclesController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadTransactions(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(vBoxCenterPane, "/main/resources/fxml/transactionsOptions.fxml", "/main/resources/css/transactionOptions.css", new TransactionsOptionsController());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Load Transactions...");
    }

    @FXML
    private void loadDashboard(MouseEvent event) {
        Main.switchToDashboard();
    }

    @FXML
    private void loadClients(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(vBoxCenterPane, "/main/resources/fxml/clientsOptions.fxml", "/main/resources/css/clientsOptions.css", new ClientsOptionsController());
        } catch (Exception e ) {
            e.printStackTrace();
        }
        System.out.println("Load clients...");
    }

    @FXML
    private void loadContabilidad(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(vBoxCenterPane, "/main/resources/fxml/contabilidadOptions.fxml", "/main/resources/css/clientsOptions.css", new ContabilidadOptionsController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadInventory(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(vBoxCenterPane, "/main/resources/fxml/inventory.fxml", "/main/resources/css/Cycles.css", new InventoryController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadProfit (MouseEvent event) {
        try {
            SceneSwitcher.switchPane(vBoxCenterPane, "/main/resources/fxml/profit.fxml", "/main/resources/css/profit.css", new ProfitController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        long startTime = System.currentTimeMillis(); 

        try {
            DatabaseUtils databaseUtils = new DatabaseUtils();

            final Double totalVes  = databaseUtils.sumColumn("saldo_actual", "bancos", "moneda = 'VES' and codigo != 'REMANENTE'");
            final Double totalDolares = databaseUtils.sumColumn("saldo_actual", "bancos", "moneda = 'USD'");
            final Double totalEuros = databaseUtils.sumColumn("saldo_actual", "bancos", "moneda = 'EUR'");
            final Double UsdtDisponible = databaseUtils.sumColumn("saldo_actual", "bancos", "moneda = 'USDT'");
            final Double totalRemanente = databaseUtils.sumColumn("saldo_actual", "bancos", "codigo = 'REMANENTE'");

            cardModels = new ArrayList<> (
                java.util.Arrays.asList (
                    new CardModel("VES", "Bolivares disponibles", String.valueOf(totalVes)),
                    new CardModel("USD", "Dolares disponibles", String.valueOf(totalDolares)),
                    new CardModel("EUR", "Euros disponibles", String.valueOf(totalEuros)),
                    new CardModel("USDT", "USDT disponibles", String.valueOf(UsdtDisponible)),
                    new CardModel("REMANENTE", "Remanente", String.valueOf(totalRemanente) + " VES")
                )
            );

            for (var cardModel : cardModels) {
                FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/main/resources/fxml/card.fxml"));
                loader.setController(new CardController());
                HBox card = loader.load();
                CardController cardController = loader.getController();
                cardController.setData(cardModel);
                hBoxCenterScrollPane.getChildren().add(card);
            }

            updateTable();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis(); // Tiempo final
        long duration = endTime - startTime; // Duración en milisegundos

        System.out.println("total time: " + duration);
    }

    private void updateTable() throws SQLException {
        BanksDAO banksDAO = new BanksDAO();
        List<BankInfo> banksList = banksDAO.getAllBankInfo();
        
        if (banksList == null || banksList.isEmpty()) {
            throw new IllegalStateException("No se encontraron datos de bancos.");
        }

        codigoTablecolumn.setCellValueFactory(new PropertyValueFactory<BankInfo, String>("code"));
        nombreBancoTablecolumn.setCellValueFactory(new PropertyValueFactory<BankInfo, String>("bankName"));
        numeroDeCuentaTablecolumn.setCellValueFactory(new PropertyValueFactory<BankInfo, String>("numberAccount"));
        saldoTablecolumn.setCellValueFactory(new PropertyValueFactory<BankInfo, String>("balance"));
        correoTablecolumn.setCellValueFactory(new PropertyValueFactory<BankInfo, String>("mail"));
        
        ObservableList<BankInfo> observableList = FXCollections.observableArrayList(banksList);

        bansTableview.setItems(observableList);
    }

    // private List<CardModel> accountBalance() throws SQLException {
    //     long startTime = System.currentTimeMillis(); // Tiempo inicial
        
    //     List<CardModel> cardModels = new ArrayList<>();
    //     BanksDAO banksDAO = new BanksDAO();
    //     List<BankInfo> bankInfoList = banksDAO.getAllBankInfo();

    //     if (bankInfoList == null || bankInfoList.isEmpty()) {
    //         throw new IllegalStateException("No se encontraron datos de bancos.");
    //     }

    //     for (BankInfo bankInfo : bankInfoList) {
    //         cardModels.add(new CardModel (
    //             bankInfo.getCode(),
    //             bankInfo.getBankName(),
    //             bankInfo.getBalance() + " " + bankInfo.getMoneyType()
    //         ));
    //     }

    //     long endTime = System.currentTimeMillis(); // Tiempo final
    //     long duration = endTime - startTime; // Duración en milisegundos

    //     System.out.println("accountBalance(): " + duration);

    //     return cardModels;
    // }

    @FXML
    List<CardModel> cardModels;
    @FXML
    private BorderPane dashBoardPane;
    @FXML
    private VBox vBoxCenterPane;
    @FXML
    private HBox hBoxCenterScrollPane;
    @FXML
    private ScrollPane scrollPaneId;

    @FXML
    private TableView<BankInfo> bansTableview;

    @FXML
    private TableColumn <BankInfo, String> codigoTablecolumn;
    @FXML
    private TableColumn <BankInfo, String> nombreBancoTablecolumn;
    @FXML 
    private TableColumn <BankInfo, String> numeroDeCuentaTablecolumn;
    @FXML
    private TableColumn <BankInfo, String> saldoTablecolumn;
    @FXML
    private TableColumn <BankInfo, String> correoTablecolumn;
}
