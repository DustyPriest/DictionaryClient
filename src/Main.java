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
            System.out.println("Connected to server");

            // DELETE TEST CASE
            msgOut.writeObject(new NetworkMessage(Status.TASK_QUERY, "hello"));
            msgOut.flush();
            try {
                NetworkMessage response = (NetworkMessage) msgIn.readObject();
                System.out.println("Server: " + response.getStatus());
            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
                System.out.println("bad data from server");
            }

            MessagePasser msgPasser = new MessagePasser(msgIn, msgOut);
            MainGUI gui = new MainGUI(msgPasser);

            while (true) {
            } // keep program running while GUI open

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
        }


    }


}