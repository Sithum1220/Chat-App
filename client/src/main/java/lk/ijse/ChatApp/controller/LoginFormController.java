package lk.ijse.ChatApp.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import lk.ijse.ChatApp.utill.Navigation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginFormController {
    public  String name;
    public JFXTextField txtName;

    private static LoginFormController controller;

    public LoginFormController(){
        controller = this;
    }

    public static LoginFormController getController(){
        return controller;
    }

    public void letsGoBtnOnAction(ActionEvent event) throws IOException {
        Navigation.switchNavigation("chatRoomForm.fxml",event);
    }
}
