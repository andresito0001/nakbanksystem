// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.controllers.LoginController;

public class Main extends Application {
    @Override
     public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/login.fxml"));
        loader.setController(new LoginController());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("/main/resources/css/login.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    public static void main (String[] args) {
        launch(args);
    }
}