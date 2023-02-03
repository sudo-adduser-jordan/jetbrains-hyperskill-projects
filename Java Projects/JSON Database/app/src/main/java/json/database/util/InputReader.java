package json.database.util;

import java.io.DataInputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;


// In this class, we create an object for getting data from the connection

public class InputReader {
    private final Socket socket;
    private DataInputStream dataInputStream;

    //Constructor of the object for getting data from the socket
    //@param socket - Socket, connection
    public InputReader(final Socket socket) {
        this.socket = socket;
        createInputStream();
    }

    public String read() {
        try {
            return dataInputStream.readUTF();
        } catch (EOFException | SocketException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[InputReader] can't read the message");
        }
        return "";
    }

    private void createInputStream() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("[InputReader] can't create a input stream");
        }

    }
}