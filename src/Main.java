import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    // IP and port
    private static String ip = "localhost";
    private static int port = 3005;

    public static void main(String[] args) {

        MainGUI clientGUI = new MainGUI();

        JOptionPane.showMessageDialog(null, "Welcome to the Dictionary App!");

//        try (Socket socket = new Socket(ip, port);) {
//            // Output and Input Stream
//            DataInputStream input = new DataInputStream(socket.getInputStream());
//
//            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
//            String sendData = "I want to connect";
//
//            output.writeUTF(sendData);
//            System.out.println("Data sent to Server--> " + sendData);
//            output.flush();
//
//            boolean flag = true;
//            while (flag) {
//                if (input.available() > 0) {
//                    String message = input.readUTF();
//                    System.out.println(message);
//                    flag = false;
//                }
//            }
//
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}