package main.java.controllers;

import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.transformation.FilteredList;
import main.java.dao.ClientsDAO;
import main.java.entities.Clients;
import main.java.util.ConnectionPool;

public class RegisterTransactionController{
    @FXML
    private ListView<String> listViewId;
    @FXML
    private TextField searchClientsBar;
    @FXML
    ObservableList<Clients> clientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {
        listViewId.setVisible(false);
        ClientsDAO clientsDAO = new ClientsDAO(ConnectionPool.getConnection());
        List<Clients> clients = clientsDAO.getClientsAsList();
        clientsList.addAll(clients);

        FilteredList<Clients> filteredClients = new FilteredList<>(clientsList, _ -> true);

        searchClientsBar.textProperty().addListener((_, _, newValue) -> {
            filteredClients.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    listViewId.setVisible(false);
                    return false;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return client.getName().toLowerCase().contains(lowerCaseFilter) ||
                       client.getLastName().toLowerCase().contains(lowerCaseFilter) ||
                       client.getCi().toLowerCase().contains(lowerCaseFilter) ||
                       client.getAlias().toLowerCase().contains(lowerCaseFilter);
            });

            listViewId.setVisible(!newValue.isEmpty());

            listViewId.setItems(FXCollections.observableArrayList(
                filteredClients.stream()
                    .map(client -> String.format("%s %s %s %s",
                        client.getName(),
                        client.getLastName(),
                        client.getCi(),
                        client.getAlias()))
                    .toList()
            ));
        });

        listViewId.setItems(FXCollections.observableArrayList(
            clientsList.stream()
                .map(client -> String.format("%s %s %s %s",
                    client.getName(),
                    client.getLastName(),
                    client.getCi(),
                    client.getAlias()))
                .toList()
        ));

        searchClientsBar.focusedProperty().addListener((_, _, newValue) -> {
            if (newValue && !searchClientsBar.getText().isEmpty()) {
                listViewId.setVisible(!searchClientsBar.getText().isEmpty());
            } else {
                listViewId.setVisible(false);
            }
        });

        listViewId.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null && !listViewId.getItems().isEmpty()) {
                searchClientsBar.setText(newValue.toString());
                System.out.println("Selected client: " + newValue);
                listViewId.setVisible(false);
            }
        });
    }
}