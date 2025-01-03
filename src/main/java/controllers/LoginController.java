package main.java.controllers;

import java.sql.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.java.dao.AdminDAO;
import main.java.util.ConnectionPool;

public class LoginController {
    public LoginController() {}

    public void loginUser(ActionEvent event) {
        try (final Connection conn = ConnectionPool.getConnection()) {
            AdminDAO admin = new AdminDAO(conn);

            if (admin.authenticateUser(userNameId.getText(), passwordId.getText())) {
                System.out.println("Usuario autenticado");
            } else {
                System.out.println("Usuario no autenticado");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField userNameId;
    @FXML
    private TextField passwordId;
    @FXML
    private Button loginButton;
}
