package file.server.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;
import static file.server.client.Action.*;

public class Main {

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final Path ROOT_PATH = Path.of(System.getProperty("user.dir"), "src", "client", "data");

    private static final int OK = 200;
    private static final int BAD_REQUEST = 400;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        createDirectoriesForFiles();

        Action action = getAction();
        if (action == null) {
            System.out.println("Unknown command");
            System.exit(1);
        }
        ServerRequest request = prepareServerRequest(action);

        try (
                Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            sendRequest(request, output);

            if (action == EXIT) {
                return;
            }

            ServerResponse serverResponse = getServerResponse(action, input);

            if (action == GET && serverResponse.getResponseCode() == OK) {
                downloadAndSaveFile(input);
            }

            printResultForUser(action, serverResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDirectoriesForFiles() {
        try {
            Files.createDirectories(ROOT_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Action getAction() {
        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        String command = scanner.nextLine();
        return Action.getActionByCommand(command);
    }

    private static ServerRequest prepareServerRequest(Action action) {
        String localFilename;
        String serverFilename;
        String request = null;
        ServerRequest serverRequest = new ServerRequest();

        if (action == PUT) {
            System.out.print("Enter name of the file: ");
            localFilename = scanner.nextLine();
            try {
                byte[] fileContentBinary = Files.readAllBytes(ROOT_PATH.resolve(localFilename));
                serverRequest.setFileContentBinary(fileContentBinary);
            } catch (NoSuchFileException e) {
                System.out.println("Cannot find the file " + e.getMessage());
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Cannot read the file " + localFilename);
                System.exit(1);
            }

            System.out.println("Enter name of the file to be saved on server: ");
            serverFilename = scanner.nextLine();
            request = PUT.request() + " " + serverFilename;
        }
        else if (action == GET || action == DELETE) {
            System.out.print("Do you want to "
                    + (action == GET ? "get" : "delete")
                    + " the file by name or by id (1 - name, 2 - id): ");
            String byNameOrId = scanner.nextLine();
            if ("1".equals(byNameOrId)) {
                System.out.print("Enter file name: ");
                serverFilename = "BY_NAME " + scanner.nextLine();
            } else {
                System.out.print("Enter id: ");
                serverFilename = "BY_ID " + scanner.nextLine();
            }
            request = action.request() + " " + serverFilename;
        }
        else if (action == EXIT) {
            request = EXIT.request();
        }

        serverRequest.setRequest(request);
        return serverRequest;
    }

    private static void sendRequest(ServerRequest request, DataOutputStream output) throws IOException {
        output.writeUTF(request.getRequest());
        if (request.getFileContentBinary() != null) {
            output.writeInt(request.getFileContentBinary().length);
            output.write(request.getFileContentBinary());
        }
        System.out.println("The request was sent.");
    }

    private static ServerResponse getServerResponse(Action action, DataInputStream input)
            throws IOException {
        String response = input.readUTF();
        Integer fileId = null;
        System.out.println("Received: " + response);

        int responseCode = -1;
        try {
            final Scanner respScanner = new Scanner(response);
            responseCode = respScanner.nextInt();
            if (action == PUT && responseCode == OK) {
                fileId = respScanner.nextInt();
            }
        } catch (InputMismatchException e) {
            System.err.println("Server responded incorrectly: " + response);
            e.printStackTrace();
        }
        return new ServerResponse(responseCode, fileId);
    }

    private static void downloadAndSaveFile(DataInputStream input) throws IOException {
        byte[] fileContentBinary;
        int len = input.readInt();
        fileContentBinary = new byte[len];
        input.readFully(fileContentBinary, 0, len);

        System.out.print("The file was downloaded! Specify a name for it: ");
        String fileName = scanner.nextLine();

        Files.write(ROOT_PATH.resolve(fileName), fileContentBinary);

        System.out.println("File saved on the hard drive!");
    }

    private static void printResultForUser(Action action, ServerResponse serverResponse) {
        String userResponse = switch (action) {
            case PUT -> serverResponse.getResponseCode() == OK ?
                    "Response says that file is saved! ID = " + serverResponse.getFileId() :
                    "The response says that creating the file was forbidden!";
            case GET -> serverResponse.getResponseCode() == OK ?
                    "" :
                    "The response says that this file is not found!";
            case DELETE -> serverResponse.getResponseCode() == OK ?
                    "The response says that the file was successfully deleted!" :
                    "The response says that this file is not found!";
            default -> "Something strange! Hmm...";
        };

        System.out.println(userResponse);
    }
}
