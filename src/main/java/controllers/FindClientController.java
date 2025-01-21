package main.java.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.java.dao.ClientsDAO;
import main.java.entities.Clients;
import main.java.util.ConnectionPool;

public class FindClientController {
    /*@FXML
    private TableColumn<Cliente,String> cedulaId; 
    @FXML
    private ObservableList<Cliente> listaClientes;
    */
    @FXML
    private ComboBox<String> filterId;
    @FXML
    private Button findButtonId;
    @FXML
    private Label filterLabelId;
    @FXML 
    private TableView<Clients> clientsTableId;
    @FXML
    private TextField clientFieldId;
    public String filtroQuery;
    public String data;
    @FXML
    private TableColumn<Clients, String> cedulaId;
    @FXML
    private TableColumn<Clients, String> nombreId;
    @FXML
    private TableColumn<Clients, String> apellidoId;
    @FXML
    private TableColumn<Clients, String> aliasId;
    @FXML
    private ObservableList<Clients> listaClientes;

    public void initialize() {
        clientsTableId.setPlaceholder(new Label (""));
        filterId.getItems().addAll("cedula", "nombre", "apellido", "alias");
        filterId.setOnAction(event -> {
            data = filterId.getSelectionModel().getSelectedItem().toString();
            filterLabelId.setText("Buscar por: " + data);
            selectFilter(data);
        });
    }
    @FXML
    public void findClient(MouseEvent event) throws SQLException {
      //  Alert alert = new Alert(AlertType.INFORMATION,"buscar " + filtroQuery + clientFieldId.getText(),ButtonType.CLOSE);
      //  alert.showAndWait();
        //listaClientsDAO = FXCollections.observableArrayList();

        //ClientsDAO.generarLista(listaClientsDAO,filtroQuery,clientFieldId.getText());
        //cliensTableId.setItems(listaClientsDAO);
        String value = clientFieldId.getText();
        listaClientes = FXCollections.observableArrayList();

       // nombre.setCellValueFactory(new PropertyValueFactory<Clients, String>(""));
        ClientsDAO clientsDAO = new ClientsDAO(ConnectionPool.getConnection());
        //Obser<Clients> clients = clientsDAO.getClientsFilter(data, filtroQuery);
        clientsDAO.getClientsFilter(filtroQuery, value, listaClientes);
        
        clientsTableId.setItems(listaClientes);

        cedulaId.setCellValueFactory(new PropertyValueFactory<Clients, String>("cedula"));
        nombreId.setCellValueFactory(new PropertyValueFactory<Clients, String>("nombre"));
        apellidoId.setCellValueFactory(new PropertyValueFactory<Clients, String>("apellido"));
        aliasId.setCellValueFactory(new PropertyValueFactory<Clients, String>("aliasProperty"));

    }
    
    public void selectFilter(String filtro) {
        filtroQuery = filtro;
    }

}
