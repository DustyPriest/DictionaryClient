import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    // IP and port
    private static String ip = "localhost";
    private static int port = 3005;

    public static void main(String[] args) {

        try (Socket socket = new Socket(ip, port)) {

            ObjectInputStream msgIn = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream msgOut = new ObjectOutputStream(socket.getOutputStream());

            MessagePasser msgPasser = new MessagePasser(msgIn, msgOut);
            MainGUI gui = new MainGUI(msgPasser);

            while (true) {
            } // keep program running while GUI open

        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Connection failed: Unknown host", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        } catch (ConnectException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection failed: Connection refused", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection failed: Connection error", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }


    }


}