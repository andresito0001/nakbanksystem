package main.test.java.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import main.java.dao.ClientsDAO;
import main.java.entities.Clients;
import main.java.services.TransactionService;
import main.java.util.ConnectionPool;
import main.test.java.transactionsAppCLI;

public class Main {
    public static void main(String[] args) throws SQLException {
        // ClientsDAO clientsDAO = new ClientsDAO(ConnectionPool.getConnection());
        
        // List<Clients> clients = clientsDAO.getClientsAsList();
        // for (Clients client : clients) {
        //     System.out.println("Cedula: " + client.getCi() + ", Alias: " + client.getAlias() + ", Nombre: " + client.getName() + ", Apellido: " + client.getLastName());
        // }

        transactionsAppCLI app = new transactionsAppCLI(ConnectionPool.getConnection());
        app.executeApp();
    }
}
