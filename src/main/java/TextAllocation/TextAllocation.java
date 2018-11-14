package TextAllocation;

import javax.json.*;
import java.util.List;

public class TextAllocation {

    public JsonArray getTexts(List<String> keywords, List<String> sources) {
        JsonArrayBuilder texts = Json.createArrayBuilder();
        for(String source : sources) {
            if(this.feedExists(source)) {
                FeedParser feedParser = new FeedParser();
                String feedUrl = this.getFeedUrl(source);
                for(JsonValue value : feedParser.getTexts(feedUrl)) {
                    JsonObject text = (JsonObject) value;
                    texts.add(text);
                }
            }
            else {
                Spider spider = new Spider();
                for(JsonValue value : spider.search(source, keywords)) {
                    JsonObject text = (JsonObject) value;
                    texts.add(text);
                }
            }
        }

        return texts.build();
    }

    private boolean feedExists(String source) { return true; }

    private String getFeedUrl(String source) { return ""; }
}
