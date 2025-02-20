package main.java.controllers;

import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.SQLException;
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

    @FXML
    public void loginUser(ActionEvent event) {
        loginButton.setDisable(true);
        long startTime = System.currentTimeMillis(); 

        Task<Boolean> loginTask = new Task<>() {
            private Connection conn;
            @Override
            protected Boolean call() throws Exception {
                try {
                    conn = ConnectionPool.getConnection();
                    
                    AdminDAO admin = new AdminDAO(conn);
                    return admin.authenticateUser(userNameId.getText(), passwordId.getText());
                } catch (SQLException e) {
                    throw new RuntimeException("Error al obtener datos de usuario", e);
                } finally {
                    if (conn != null) {
                        ConnectionPool.releaseConnection(conn);
                    }
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
                long endTime = System.currentTimeMillis(); // Tiempo final
                long duration = endTime - startTime; // Duración en milisegundos

                System.out.println(duration);
            } else {
                errorMsg.setText("* Usuario o contraseña invalidos");
                loginButton.setDisable(false);

                long endTime = System.currentTimeMillis(); // Tiempo final
                long duration = endTime - startTime; // Duración en milisegundos

                System.out.println(duration);
            }
        });

        loginTask.setOnFailed(_ -> {
            loginTask.getException().printStackTrace();
        });

        new Thread(loginTask).start();
    }
}