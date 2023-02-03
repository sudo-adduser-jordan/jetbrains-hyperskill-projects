package file.server.server;


import java.nio.file.Path;

public class Main {

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final Path ROOT_PATH = Path.of(System.getProperty("user.dir"), "src", "server", "data");

    public static void main(String[] args) {

        System.out.println("Server started!");

        WebServer webServer = new WebServer(ADDRESS, PORT, ROOT_PATH);
        webServer.start();
    }

}