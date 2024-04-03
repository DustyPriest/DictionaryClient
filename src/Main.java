import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    // IP and port
    private static String ip;
    private static int port;

    public static void main(String[] args) {

        // USAGE: java -jar DictionaryClient.jar <server-address> <port>
        if (!checkArgs(args)) {
            System.out.println("Usage: java -jar DictionaryClient.jar <server-address> <port>");
            System.exit(0);
        }

//        ip = args[0];
//        port = Integer.parseInt(args[1]);

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

    private static boolean checkArgs(String[] args) {
        if (args.length != 2) {
            return false;
        }
        String ipRegex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
        if (!args[0].matches(ipRegex) && !args[0].equals("localhost")) {
            return false;
        }
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        ip = args[0];
        return true;
    }


}