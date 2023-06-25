package lk.ijse.ChatApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginForm.fxml"));
        stage.setTitle("Chat App Vision 1.0.1");
        stage.setScene(new Scene(root));
        stage.show();
    }
}