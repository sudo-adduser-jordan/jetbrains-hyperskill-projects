package hypermetro;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Utils {

    public static Map<String, List<Station>> loadFromJson(String filePath) {
        String jsonString = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                jsonString += line;
            }
        }
        catch (IOException e)
        {
            System.out.print("Error");
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type empMapType = new TypeToken<Map<String, List<Station>>>() {}.getType();
        Map<String, List<Station>> metros = gson.fromJson(jsonString, empMapType);


        return metros;
    }

}
