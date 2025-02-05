package main.java.controllers;

import javafx.scene.control.Label;
import java.sql.Connection;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.java.dao.AdminDAO;
import main.java.util.ConnectionPool;
import main.java.Main;

public class LoginController {
    @FXML
    private TextField userNameId;
    @FXML
    private TextField passwordId;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorMsg;

    public LoginController() {}

    @FXML
    public void loginUser(ActionEvent event) {
        Task<Boolean> loginTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                try (final Connection conn = ConnectionPool.getConnection()) {
                    AdminDAO admin = new AdminDAO(conn);
                    return admin.authenticateUser(userNameId.getText(), passwordId.getText());
                }
            }
        };

        loginTask.setOnSucceeded(_ -> {
            if (loginTask.getValue()) {
                errorMsg.setText("");
                Main.setUsername(userNameId.getText());
                userNameId.clear();
                passwordId.clear();
                Main.switchToDashboard();
                System.out.println("Usuario autenticado");
            } else {
                errorMsg.setText("* Usuario o contraseña invalidos");
            }
        });

        loginTask.setOnFailed(_ -> {
            loginTask.getException().printStackTrace();
        });

        new Thread(loginTask).start();
    }
}