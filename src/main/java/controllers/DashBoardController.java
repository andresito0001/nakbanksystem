package main.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.Main;
import main.java.dao.BanksDAO;
import main.java.entities.CardModel;
import main.java.util.ConnectionPool;
import main.java.util.SceneSwitcher;

public class DashBoardController  implements Initializable{
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

// public void initialize() {
//     vBoxCenterPane.getChildren().clear();

//     Label loadingLabel = new Label("Loading...");
//     loadingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray; -fx-backbround-color: transparent;");
//     vBoxCenterPane.getChildren().add(loadingLabel);
    
//     Task<Void> loadDashboardTask = new Task<>() {
//         @Override
//         protected Void call() throws Exception {
//             try (final Connection conn = ConnectionPool.getConnection()) {
//                 PreparedStatement st = conn.prepareStatement("SELECT * FROM bancos");
//                 ResultSet rs = st.executeQuery();

//                     HBox hBox = new HBox(10);
//                     int cardCount = 0;

//                 while (rs.next()) {
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/card.fxml"));
//                    VBox card = loader.load();

//                    CardController cardController = loader.getController();
//                    cardController.setCodigo(rs.getString("codigo") + " - " + rs.getString("nombre_banco"));
//                    cardController.setSaldo(rs.getString("saldo_actual") + " " + rs.getString("moneda"));
                   
//                    // Importante: actualizamos la interfaz usando Platform.runLater
//                     Platform.runLater(() -> {
//                         vBoxCenterPane.getChildren().remove(loadingLabel);
//                         vBoxCenterPane.getChildren().add(card);
//                     });
//                 }
//             } catch (Exception e) {
//                 e.printStackTrace();
//                 throw e; // Lanzamos la excepción para que se capture en el `Task`.
//             }
//             return null;
//         }
//     };

//         // Mostrar mensaje de carga mientras se ejecuta el Task
//         loadDashboardTask.setOnRunning(e -> {
//             vBoxCenterPane.getChildren().clear();
//             vBoxCenterPane.getChildren().add(loadingLabel);
//         });

//     // Manejamos el éxito o el fallo del Task
//     loadDashboardTask.setOnSucceeded(e -> {
//         System.out.println("Carga de datos completada");
//     });

//     loadDashboardTask.setOnFailed(e -> {
//         vBoxCenterPane.getChildren().clear();
//         loadDashboardTask.getException().printStackTrace();
//     });

//     // Iniciamos el Task en un nuevo hilo
//     new Thread(loadDashboardTask).start();
// }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<CardModel> accountBalance() throws SQLException {
        List<CardModel> cardModels = new ArrayList<>();
        List<String> codesList, bankNamesList, balancesList, typeMoneyList;

        BanksDAO banksDAO = new BanksDAO(ConnectionPool.getConnection());
        codesList = banksDAO.getInfoOf("codigo", null, null);
        bankNamesList = banksDAO.getInfoOf("nombre_banco", null, null);
        balancesList = banksDAO.getInfoOf("saldo_actual", null, null);
        typeMoneyList = banksDAO.getInfoOf("moneda", null, null);
        for (int i = 0; i < codesList.size(); i++) {
            cardModels.add(new CardModel(codesList.get(i),
            bankNamesList.get(i),
            balancesList.get(i) + " " + typeMoneyList.get(i)));
        }

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
}
