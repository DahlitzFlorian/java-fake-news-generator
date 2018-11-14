package Configuration;

import javax.json.*;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Tim Leuschner
 */

public class Configuration {

    private static final String CONFIG_PATH = "config.json";

    public List<String> getSources(JsonObject config) {
        return config.getJsonArray("sources").getValuesAs(JsonValue::toString);
    }

    public JsonObject getTextConfigurations() throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(CONFIG_PATH)), StandardCharsets.UTF_8);
        JsonReader jsonReader = Json.createReader(new StringReader(text));
        JsonObject result = jsonReader.readObject();
        jsonReader.close();

        return result;
    }

    public void saveConfiguration(JsonObject config) throws IOException {
        Files.write(Paths.get(CONFIG_PATH), config.toString().getBytes());
    }
}
