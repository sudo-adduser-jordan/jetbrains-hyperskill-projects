package json.database.server;


import com.google.gson.Gson;

//Auxiliary class for extracting keys and objects from JSON messages.
public class GsonFromJSON {
    String json_string;
    Arguments arguments;

    //Constructor
    //@param json_string - String, JSON message
    public GsonFromJSON(String json_string) {
        this.json_string = json_string;
    }

    //Get and set a set of variables for the Arguments
    //class from an accepted JSON string.
    //@return boolean true if got it
    public boolean getString() {
        Gson gson = new Gson();
        arguments = gson.fromJson(json_string, Arguments.class);
        return arguments != null;
    }

    public String getType() {
        return arguments.getType();
    }

    public String getKey() {
        return arguments.getKey();
    }

    public String getValue() {
        return arguments.getValue();
    }
}