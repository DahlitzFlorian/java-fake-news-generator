package TextAllocation;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public interface RSSFeedParserInterface {
    public JsonObject articleCollector(String keyword);
    public String articleCutter(String article);
    public JsonObjectBuilder articleJSONBuilder(JsonObjectBuilder articleJson, String News, String title);
}
