package main.java.controllers;

import javafx.scene.control.Label;
import java.sql.SQLException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import main.java.dao.AdminDAO;
import main.java.util.SceneSwitcher;
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
    private Pane hboxpane;

    @FXML
    public void loginUser(ActionEvent event) {
        loginButton.setDisable(true);
        long startTime = System.currentTimeMillis(); 

        Task<Boolean> loginTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    AdminDAO admin = new AdminDAO();
                    return admin.authenticateUser(userNameId.getText());
                } catch (SQLException e) {
                    throw new RuntimeException("Error al obtener datos de usuario", e);
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

        loginTask.setOnRunning(_ -> { 
            try {
                SceneSwitcher.switchPane(hboxpane, "/main/resources/fxml/loading.fxml", "/main/resources/css/loading.css", new loadingController());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loginTask.setOnFailed(_ -> {
            loginTask.getException().printStackTrace();
        });

        new Thread(loginTask).start();
    }
}