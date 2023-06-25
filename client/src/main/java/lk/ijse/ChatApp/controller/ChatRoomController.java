package lk.ijse.ChatApp.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lk.ijse.ChatApp.utill.Navigation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatRoomController implements Initializable {

    public Pane menuPane;
    public Pane Pane;
    public Pane msgTxtField;
    public Label txtName;
    public JFXTextField txtMsg;
    public VBox msgVbox;
    public ScrollPane scrollPane;
    BufferedReader in;
    PrintWriter out;
    Socket socket;

    public void addOnAction(MouseEvent event) throws IOException {
        Navigation.popupNavigation("loginForm.fxml");
    }

    public void logoutOnAction(MouseEvent mouseEvent) {
        Navigation.close(mouseEvent);
    }

    public void sendOnAction(ActionEvent event) {
        String messageToSend = txtMsg.getText();
        out.println(messageToSend);
//        System.out.println(messageToSend);
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                    "-fx-background-color: rgb(15,125,242);" +
                    " -fx-background-radius: 20px");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));

            hBox.getChildren().add(textFlow);
            msgVbox.getChildren().add(hBox);
            txtMsg.clear();
        }
    }

    public void receivedMsg(String msg) {
        String[] tokens = msg.split(":");
        String cmd = tokens[0];
        if (!txtName.getText().equals(cmd)) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(msg);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                    " -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));

            hBox.getChildren().add(textFlow);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    msgVbox.getChildren().add(hBox);
                }
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtName.setText(LoginFormController.getController().txtName.getText());
        try {
            socket = new Socket("localhost", 9801);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                System.out.println("Run");
                try {
                    while (true) {
                        String line = in.readLine();
                        if (line.startsWith("SUBMITNAME")) {
                            out.println(LoginFormController.getController().txtName.getText());
                        } else if (line.startsWith("MESSAGE")) {
                            System.out.println(line.substring(7) + "\n");
                            receivedMsg(line.substring(7) + "\n");
                        } else {
                        Navigation.popupNavigation("loginForm.fxml");
                        Navigation.exit();
                    }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
