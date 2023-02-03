package file.server.client;

import java.util.Arrays;

public enum Action {
    GET("1", "GET"),
    PUT("2", "PUT"),
    DELETE("3", "DELETE"),
    EXIT("exit", "EXIT");

    private String textCommand;
    private String request;

    Action(String textCommand, String request) {
        this.textCommand = textCommand;
        this.request = request;
    }

    public String getTextCommand() {
        return textCommand;
    }

    public String request() {
        return request;
    }

    public static Action getActionByCommand(String command) {
        return Arrays.stream(Action.values())
                .filter(action -> action.getTextCommand().equals(command))
                .findFirst()
                .orElse(null);
    }

}
