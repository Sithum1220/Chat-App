package lk.ijse.ChatApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class Handle extends Thread {


    private ArrayList<Handle> clients;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Handle(Socket socket,  ArrayList<Handle> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (msg.equalsIgnoreCase( "exit")) {
                    break;
                }
                for (Handle cl : clients) {
                    cl.writer.println(msg);
                }
            }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                 e.printStackTrace();
            }
        }

    }
}
