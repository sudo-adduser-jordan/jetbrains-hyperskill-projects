package file.server.server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    public static final String EXIT_REQ = "EXIT";
    public static final String PUT_REQ = "PUT";
    public static final String GET_REQ = "GET";
    public static final String DELETE_REQ = "DELETE";

    private static final int OK = 200;
    private static final int BAD_REQUEST = 400;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;

    private final String address;
    private final int port;
    private final Path rootDir;

    private FileServer fileServer;
    private ExecutorService executor;
    private ServerSocket serverSocket;

    public WebServer(String address, int port, Path rootDir) {
        this.address = address;
        this.port = port;
        this.rootDir = rootDir;
    }

    private void initialize() {
        fileServer = new FileServer(rootDir);
        executor = Executors.newCachedThreadPool();
    }

    public void start() {
        initialize();

        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));

            while (true) {
                Socket socket = serverSocket.accept();
                executor.submit(() -> handleClientRequest(socket));
            }
        } catch (SocketException e) {

            System.err.println("Shutdown! Received SocketException in webServer.start() - serverSocket.accept()");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        executor.shutdownNow();
        fileServer.stop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void handleClientRequest(Socket socket) {
        DataInputStream input = null;
        DataOutputStream output = null;
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            String received = input.readUTF();
            System.out.println(LocalTime.now() + " - Received: " + received);

            if (received.equals(EXIT_REQ)) {
                stop();
            }

            Scanner scanner = new Scanner(received);
            String request = scanner.next();

            Integer fileId = null;
            String filename = "";
            boolean byId = false;

            if (List.of(GET_REQ, DELETE_REQ).contains(request)) {
                String byNameOrId = scanner.next();
                byId = "BY_ID".equals(byNameOrId);
                if (byId) {
                    fileId = scanner.nextInt();
                } else {
                    filename = scanner.next();
                }
            } else if (request.equals(PUT_REQ)) {
                filename = scanner.hasNext() ? scanner.next() : "";
            }
            byte[] fileContentBinary = null;
            String response;

            switch (request) {
                case PUT_REQ -> {
                    int length = input.readInt();
                    fileContentBinary = new byte[length];
                    input.readFully(fileContentBinary, 0, length);

                    fileId = fileServer.put(filename, fileContentBinary);
                    response = "" + (fileId > 0 ? OK + " " + fileId : FORBIDDEN);
                }
                case GET_REQ -> {
                    fileContentBinary = byId ? fileServer.get(fileId) : fileServer.get(filename);
                    response = "" + (fileContentBinary == null ? NOT_FOUND : OK);
                }
                case DELETE_REQ -> {
                    boolean res = byId ? fileServer.delete(fileId) : fileServer.delete(filename);
                    response = "" + (res ? OK : NOT_FOUND);
                }
                default -> response = "" + BAD_REQUEST;
            }

            output.writeUTF(response);
            if (request.equals(GET_REQ) && fileContentBinary != null) {
                output.writeInt(fileContentBinary.length);
                output.write(fileContentBinary);
            }
            System.out.println(LocalTime.now() + " - Sent: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
