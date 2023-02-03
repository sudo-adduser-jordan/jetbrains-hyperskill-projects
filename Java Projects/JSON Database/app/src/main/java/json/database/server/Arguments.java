package json.database.server;

//This is an auxiliary class for generating JSON message arguments
public class Arguments {
    private final String type;
    private final String key;
    private final String value;
    private final String fileName;

    public Arguments(String type, String key, String value, String fileName) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFileName() {
        return fileName;
    }
}