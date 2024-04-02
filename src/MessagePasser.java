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

    public NetworkMessage sendMessage(NetworkMessage msg) {
        try {
            msgOut.writeObject(msg);
            msgOut.flush();
            return receiveMessage();
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("server disconnected");
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
