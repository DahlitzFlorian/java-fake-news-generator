package Configuration;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.LinkedList;
import java.util.List;

public class Configuration {

    public List<String> getSources() { return new LinkedList<String>(); }

    public JsonObject getTextConfigurations() { return Json.createObjectBuilder().build(); }
}
