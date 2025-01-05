package gson;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VariableManager {
    private static final String FILE_PATH = "variables.json";
    private static final Gson gson = new Gson();
    private static JsonObject jsonData;

    static {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            jsonData = gson.fromJson(reader, JsonObject.class);
            if (jsonData == null) jsonData = new JsonObject();
        } catch (IOException e) {
            jsonData = new JsonObject();
        }
    }
    private static void saveToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(jsonData, writer);
        } catch (IOException e) {
            System.err.println("Failed to save JSON file: " + e.getMessage());
        }
    }

    // Set a variable
    public static void setVariable(String name, String value) {
        jsonData.addProperty(name, value);
        saveToFile();
    }

    public static String getVariable(String name) {
        return jsonData.has(name) ? jsonData.get(name).getAsString() : null;
    }
    public static void removeVariable(String name) {
        if (jsonData.has(name)) {
            jsonData.remove(name);
            saveToFile();
        }
    }

}