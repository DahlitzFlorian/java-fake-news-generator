package TextAllocation;

import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TextAllocation {

    public JsonArray getTexts(String[] keywords, List<String> sources) {
        JsonArrayBuilder texts = Json.createArrayBuilder();
        for(String source : sources) {
            List<String> feedUrls = this.getFeedUrls(source);

            if(feedUrls.size() != 0) {
                FeedParser feedParser = new FeedParser();

                for(String feedUrl : feedUrls) {
                    for (JsonValue value : feedParser.getTexts(feedUrl, keywords)) {
                        JsonObject text = (JsonObject) value;
                        texts.add(text);
                    }
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

    private List<String> getFeedUrls(String source) {
        List<String> feedUrls = new ArrayList<>();
        try {
            URL url = new URL(source);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                if(line.contains("<link rel=\"alternate\"") && line.contains("type=\"application/rss+xml\"")) {
                    feedUrls.add(line.split("href=")[1].split("\"")[1]);
                }
            }

            return feedUrls;
        } catch (IOException ioe) {
            return feedUrls;
        }
    }
}
