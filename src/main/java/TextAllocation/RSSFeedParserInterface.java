package TextAllocation;

import org.json.JSONObject;

public interface RSSFeedParserInterface {
    public JSONObject articleCollector(String keyword);
    public String articleCutter(String article);
    public JSONObject articleJSONBuilder(JSONObject articleJson, String News, String title);
}
