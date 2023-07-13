package lk.ijse.ChatApp;

import com.vdurmont.emoji.EmojiParser;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Test {
    public static void main(String[] args) {

        String msg = "ji"+":"+"\uD83D\uDE00";

        if (msg.startsWith("ji")){
            System.out.println("if");
            String[] split = msg.split(":");
            String emoji = split[1];

            Text text = new Text(emoji);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle(
                    "-fx-font-size: 50");

            System.out.println(text);
        }
    }
}