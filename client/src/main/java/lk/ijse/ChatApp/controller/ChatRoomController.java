package lk.ijse.ChatApp.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.ChatApp.utill.Navigation;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatRoomController implements Initializable {

    public Label txtName;
    public JFXTextField txtMsg;
    public VBox msgVbox;
    public AnchorPane EmojiPane;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private FileChooser fileChooser;
    private File filePath;

    public void addOnAction(MouseEvent event) throws IOException {
        Navigation.popupNavigation("loginForm.fxml");
    }

    public void logoutOnAction(MouseEvent mouseEvent) {
        Navigation.close(mouseEvent);
    }

    public void sendOnAction(MouseEvent event) {
        String messageToSend = txtMsg.getText();
        out.println(txtName.getText() + ": " + messageToSend);
//        System.out.println(messageToSend);
        EmojiPane.setVisible(false);

    }

    public void sendMsg(String messageToSend) {
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

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    msgVbox.getChildren().add(hBox);
                    txtMsg.clear();
                }
            });

        }
    }

    public void senderSideEmoji(String messageToSend){
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                    "-fx-font-size: 40;"+
                    "-fx-background-color: rgb(15,125,242);" +
                    " -fx-background-radius: 20px");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));

            hBox.getChildren().add(textFlow);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    msgVbox.getChildren().add(hBox);
                    txtMsg.clear();
                }
            });

        }
    }

    public void receivedMsg(String msg) {
        String[] tokens = msg.split(":");
        String cmd = tokens[0];

        if (!txtName.getText().equals(cmd)) {
            System.out.println("ert");
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(msg);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                    "-fx-background-radius: 20px");
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
            socket = new Socket("localhost", 6705);
            System.out.println("Socket is connected with server!");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                while (true) {
                    try {
                        String fullMsg = in.readLine();
                        String[] split = fullMsg.split(":");
                        String name = split[0];
                        String msgOnly = split[1];
                        System.out.println(name + "name");
                        String firstChar = " ";
                        if (msgOnly.length() > 3) {
                            firstChar = msgOnly.substring(0, 4);
                            System.out.println("firstChaR" + firstChar);
                        }

                        if (firstChar.equalsIgnoreCase(" img")) {
                            String[] splitMsgOnly = fullMsg.split("img");
                            String path = splitMsgOnly[1];

                            System.out.println("Path " + path);


                            File file = new File(path);
                            Image image = new Image(file.toURI().toString());

                            ImageView imageView = new ImageView(image);

                            imageView.setFitHeight(300);
                            imageView.setFitWidth(300);

                            HBox hBox = new HBox(10);
                            hBox.setAlignment(Pos.BOTTOM_RIGHT);

                            if (txtName.getText().equalsIgnoreCase(name)) {
                                hBox.setAlignment(Pos.TOP_RIGHT);
                                hBox.getChildren().add(imageView);
                                Text text1 = new Text(": Me ");
                                hBox.getChildren().add(text1);
                            } else {
                                msgVbox.setAlignment(Pos.TOP_LEFT);
                                hBox.setAlignment(Pos.TOP_LEFT);

                                Text text1 = new Text("  " + name + " :");
                                hBox.getChildren().add(text1);
                                hBox.getChildren().add(imageView);
                            }

                            Platform.runLater(() -> msgVbox.getChildren().addAll(hBox));

                        }else {
                            if (txtName.getText().equalsIgnoreCase(name)) {
                                sendMsg(msgOnly);
                            } else {
                                receivedMsg(fullMsg);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void emojiOnMouseClick(MouseEvent mouseEvent) {
        EmojiPane.setVisible(true);
    }

    public void imgSendOnMouseClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        out.println(txtName.getText() + ": img" + filePath.getPath());
        System.out.println(filePath.getPath());

    }

    public void Heart(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128525));
        txtMsg.appendText(emoji);

    }

    public void sadMood(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128546));
        txtMsg.appendText(emoji);

    }

    public void normalMood(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars( 128522));
        txtMsg.appendText(emoji);

    }

    public void Hehe(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128513));
        txtMsg.appendText(emoji);

    }

    public void ToungOut(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128539));
        txtMsg.appendText(emoji);

    }

    public void sick(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128560));
        txtMsg.appendText(emoji);
    }

    public void Hiks(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128540));
        txtMsg.appendText(emoji);

    }

    public void soSad(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128554));
        txtMsg.appendText(emoji);

    }

    public void haha(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128514));
        txtMsg.appendText(emoji);

    }

    public void Emotional(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128578));
        txtMsg.appendText(emoji);

    }

    public void bad(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128543));
        txtMsg.appendText(emoji);

    }

    public void money(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(129297));
        txtMsg.appendText(emoji);

    }

    public void satisfied(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128519));
        txtMsg.appendText(emoji);

    }

    public void ohh(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128550));
        txtMsg.appendText(emoji);

    }

    public void wow(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128559));
        txtMsg.appendText(emoji);
    }
}
