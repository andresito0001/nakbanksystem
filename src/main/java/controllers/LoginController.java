package main.java.controllers;

import javafx.scene.control.Label;
import java.sql.SQLException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
                Alert alert = new Alert(AlertType.ERROR, "Usuario o clave incorrectas. ");
                alert.showAndWait();

                try {
                    SceneSwitcher.switchPane(hboxpane, "/main/resources/fxml/login.fxml", "/main/resources/css/login.css", new LoginController());
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                Alert alert = new Alert(AlertType.WARNING, "Ha ocurrido un error al acceder al sistema. ");
                alert.showAndWait();
            }
        });

        loginTask.setOnFailed(_ -> {
            loginTask.getException().printStackTrace();
            try {
                Alert alert = new Alert(AlertType.ERROR, "Ha ocurrido un error de conexion");
                alert.showAndWait();
                SceneSwitcher.switchPane(hboxpane, "/main/resources/fxml/login.fxml", "/main/resources/css/login.css", new LoginController());
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR, "Ha ocurrido un error. Reinicie");
                alert.showAndWait();
            }
        });

        new Thread(loginTask).start();
    }
}