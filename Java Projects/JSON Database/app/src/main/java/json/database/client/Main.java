package json.database.client;

import com.google.gson.Gson;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import json.database.util.InputReader;
import json.database.util.OutputWriter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Socket clientSocket;

    @Parameter(names = {"--type", "-t"}, description = "command")
    String type;
    @Parameter(names = {"--key", "-k"}, description = "key")
    String key;
    @Parameter(names = {"--value", "-v"}, description = "value object")
    String value;
    @Parameter(names = {"--input", "-in"}, description = "file name")
    String fileName;

    Map<String, String> request = new LinkedHashMap<>();

    public static void main(final String[] args) {

        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();
    }

    public void run() {

        createSocket();

        InputReader inputReader = new InputReader(clientSocket);
        OutputWriter outputWriter = new OutputWriter(clientSocket);

        System.out.println();
        System.out.println("Client started!");

        String output = "";

        if (fileName == null) {

            createRequest(type, key, value);

            Gson gson = new Gson();

            output = gson.toJson(request);

        } else {

            try {
                File file = new File("C:\\Users\\profile1\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\client\\data\\" + fileName);
                Scanner scanner = new Scanner(file);
                output = scanner.nextLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Sent: " + output);
        outputWriter.sentMessage(output);

        String receivedMessage = inputReader.read().trim();
        System.out.println("Received: " + receivedMessage);

        closeSocket();
    }

     //Forming a request Map based on the available parameters
     //@param type  - String, command
     //@param key   - String, key of Map
     //@param value - String, value of key
    private void createRequest(String type, String key, String value) {

        request.put("type", type);
        if (key != null) {
            request.put("key", key);
        }
        if (value != null) {
            request.put("value", value);
        }

    }


     //Close the connection
    private static void closeSocket() {
        try {
            clientSocket.close();
        } catch (Exception ignored) {
        }
    }


     //Creating a socket to connect to the server
    private static void createSocket() {
        final String address = "127.0.0.1";
        final int port = 23456;
        while (true) {
            try {
                clientSocket = new Socket(InetAddress.getByName(address), port);
                return;
            } catch (Exception e) {
                System.out.println("\n" + e + "\n[CLIENT] Can't connect to the server");
            }

        }
    }
}