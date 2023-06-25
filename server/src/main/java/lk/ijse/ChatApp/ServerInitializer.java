package lk.ijse.ChatApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class ServerInitializer {
    public static ArrayList<String> names = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        System.out.println("Chat Server is running");
        ServerSocket lostner = new ServerSocket(9801);

        try {
            while (true){
                Socket socket = lostner.accept();
                System.out.println("Client Connected");
                Thread clientThread = new Thread(new Handle(socket));
                clientThread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lostner.close();
        }
    }
}