// MessagePasser
// Handles communication with the server and parsing of messages

import javax.swing.*;
import java.io.*;
import java.net.SocketException;

public class MessagePasser {

    //    private final InputStream in;
    private final ObjectInputStream msgIn;
    private final ObjectOutputStream msgOut;

    public MessagePasser(ObjectInputStream msgIn, ObjectOutputStream msgOut) {
        this.msgIn = msgIn;
        this.msgOut = msgOut;
    }

    public NetworkMessage sendMessage(Status status, String[] data) {
        NetworkMessage msg = new NetworkMessage(status, data);
        try {
            msgOut.writeObject(msg);
            msgOut.flush();
            return receiveMessage();
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Connection to server lost, program will exit...", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private NetworkMessage receiveMessage() throws IOException {
        // wait for response after sending a message
        try {
            return (NetworkMessage) msgIn.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("bad data from server");
            System.exit(0);
        }
        return null;
    }


}
