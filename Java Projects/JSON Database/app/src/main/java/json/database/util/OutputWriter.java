package json.database.util;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;

//In this class, we create an object for sending data in the connection

public class OutputWriter {
    private final Socket socket;
    private DataOutputStream dataOutputStream;

    //Constructor of the object for sending data to the socket
    //@param socket - Socket, connection
    public OutputWriter(final Socket socket) {
        this.socket = socket;
        createOutputStream();
    }

    public void sentMessage(final String message) {
        try {
            dataOutputStream.writeUTF(message);
        } catch (EOFException | SocketException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[OutputWriter] can't write the message");
        }
    }

    private void createOutputStream() {
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("[outputWriter] can't create a output stream");
        }

    }
}