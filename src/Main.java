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

//        Thread guiThread = new Thread(MainGUI::new);
//        guiThread.start();

//        JOptionPane.showMessageDialog(null, "Welcome to the Dictionary App!");
        Socket socket = null;

        try {
            socket = new Socket(ip, port);

            // Input and Output streams; base input stream used to check for available bytes
            ObjectOutputStream msgOut = new ObjectOutputStream(socket.getOutputStream());
            InputStream in = socket.getInputStream();
            ObjectInputStream msgIn = new ObjectInputStream(in);
//            System.out.println("Connected to server");

            // DELETE TEST CASE
            msgOut.writeObject(new Message(Status.TASK_QUERY, "hello"));
            msgOut.flush();
            try {
                Message response = (Message) msgIn.readObject();
                System.out.println("Server: " + response.getStatus());
            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
                System.out.println("bad data from server");
            }


            while (true) {
                if (in.available() > 0) {
                    try {
                        Message response = (Message) msgIn.readObject();
                        System.out.println(response.getData());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        System.out.println("bad data from server");
                        // TODO fix stream when broken
                    }
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
//            System.out.println("Connection failed");
//            System.exit(0);
        } catch (ConnectException e) {
//            e.printStackTrace();
            System.out.println("Connection failed");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}