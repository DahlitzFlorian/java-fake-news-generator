package TextAllocation;

import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

public class TextAllocation {

    public JsonArray getTexts(String[] keywords, List<String> sources) {
        JsonArrayBuilder texts = Json.createArrayBuilder();
        for(String source : sources) {
            //String feedUrl = this.getFeedUrl(source);
            String feedUrl = source.replaceAll("\"", "");
            if(!feedUrl.equals("")) {
                FeedParser feedParser = new FeedParser();
                for(JsonValue value : feedParser.getTexts(feedUrl, keywords)) {
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

    //TODO get it working
    private String getFeedUrl(String source) {
        try {
            URL url = new URL(source);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains("<link rel=\"alternate\" type=\"application/rss+xml\"")) {
                    return line.split("href=")[1].split("\"")[1];
                }
            }

            return "";
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return "";
        }
    }
}
