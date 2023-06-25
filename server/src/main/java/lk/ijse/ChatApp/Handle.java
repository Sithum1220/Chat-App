package lk.ijse.ChatApp;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

public class Handle extends Thread {

    private String name;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public static HashSet<PrintWriter> writers = new HashSet<>();

    public Handle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Client run");
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                out.println("SUBMITNAME");
                name = in.readLine();
                System.out.println("name "+name);
                if (name == null) {
                    return;
                }
                if (!ServerInitializer.names.contains(ServerInitializer.names)) {
                    ServerInitializer.names.add(name);
                    break;
                }
            }

            writers.add(out);
            System.out.println("qqq");
            while (true) {
                System.out.println("while");
                String input = in.readLine();
                System.out.println("msg "+input);

                if (input == null) {
                    return;
                }
                for(PrintWriter writer : writers){
                    System.out.println("For");
                   writer.println("MESSAGE"+name+": "+input);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
             if (ServerInitializer.names != null){
                 ServerInitializer.names.remove(name);
             }
             if (out != null){
                 writers.remove(out);
             }

            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
