package main.java.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import main.java.dao.BanksDAO;
import main.java.util.ConnectionPool;

public class RegisterExpenseController {
    @FXML 
    private ComboBox<String> metodoPagoId;

    public void initialize() throws SQLException{
        List<String> bankCodes = new ArrayList<>();
        bankCodes = new BanksDAO(ConnectionPool.getConnection()).getInfoOf("codigo", null, null);
        
        metodoPagoId.getItems().addAll(bankCodes); 
    }
    
}
