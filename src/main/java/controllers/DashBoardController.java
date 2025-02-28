package main.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.entities.BankInfo;
import main.java.entities.CardModel;
import main.java.util.SceneSwitcher;

public class DashBoardController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        long startTime = System.currentTimeMillis(); 

        try {
            cardModels = new ArrayList<>(accountBalance());

            for (CardModel cardModel : cardModels) {
                FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/main/resources/fxml/card.fxml"));
                loader.setController(new CardController());
                HBox card = loader.load();
                CardController cardController = loader.getController();
                cardController.setData(cardModel);
                hBoxCenterScrollPane.getChildren().add(card);
            }


        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis(); // Tiempo final
        long duration = endTime - startTime; // Duración en milisegundos

        System.out.println("total time: " + duration);
    }

    private List<CardModel> accountBalance() throws SQLException {
        long startTime = System.currentTimeMillis(); // Tiempo inicial
        
        List<CardModel> cardModels = new ArrayList<>();
        BanksDAO banksDAO = new BanksDAO();
        List<BankInfo> bankInfoList = banksDAO.getAllBankInfo();

        if (bankInfoList == null || bankInfoList.isEmpty()) {
            throw new IllegalStateException("No se encontraron datos de bancos.");
        }

        for (BankInfo bankInfo : bankInfoList) {
            cardModels.add(new CardModel (
                bankInfo.getCode(),
                bankInfo.getBankName(),
                bankInfo.getBalance() + " " + bankInfo.getMoneyType()
            ));
        }

        long endTime = System.currentTimeMillis(); // Tiempo final
        long duration = endTime - startTime; // Duración en milisegundos

        System.out.println("accountBalance(): " + duration);

        return cardModels;
    }

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
}
