package main.java.controllers;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.java.dao.ClientsDAO;
import main.java.entities.Clients;
import main.java.util.ConnectionPool;
import main.java.util.SceneSwitcher;

public class FindClientController {

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

        String value = clientFieldId.getText();
        listaClientes = FXCollections.observableArrayList();

        ClientsDAO clientsDAO = new ClientsDAO(ConnectionPool.getConnection());

        clientsDAO.getClientsFilter(filtroQuery, value, listaClientes);
        
        clientsTableId.setItems(listaClientes);

        cedulaId.setCellValueFactory(new PropertyValueFactory<Clients, String>("cedula"));
        nombreId.setCellValueFactory(new PropertyValueFactory<Clients, String>("nombre"));
        apellidoId.setCellValueFactory(new PropertyValueFactory<Clients, String>("apellido"));
        aliasId.setCellValueFactory(new PropertyValueFactory<Clients, String>("aliasProperty"));

        if (listaClientes.isEmpty() == true) {
            clientsTableId.setPlaceholder(new Label("No hay clientes que coincidan con la busqueda "));
        }

    }
    
    public void selectFilter(String filtro) {
        filtroQuery = filtro;
    }

    @FXML
    public void back(MouseEvent event) {
        try {
            SceneSwitcher.switchPane(anchorpane, "/main/resources/fxml/clientsOptions.fxml", "/main/resources/css/clientOptions.css", new ClientsOptionsController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Pane anchorpane;

}
