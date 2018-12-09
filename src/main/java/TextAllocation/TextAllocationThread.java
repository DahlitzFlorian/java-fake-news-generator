package TextAllocation;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class which is called to fetch articles in parallel
 *
 * @author Dahlitz
 */
public class TextAllocationThread implements Runnable {

    private JsonArrayBuilder texts;
    private String source;
    private String[] keywords;

    /**
     *
     * @param source Source articles are fetched from
     * @param keywords Keywords searched for
     * @param texts Reference to the final texts object
     */
    TextAllocationThread(String source, String[] keywords, JsonArrayBuilder texts) {
        this.texts = texts;
        this.source = source;
        this.keywords = keywords;
    }

    /**
     * Required by the Runnable interface and used to run the tasks in parallel.
     */
    public void run() {
        this.source = this.source.replaceAll("\"", "");
        List<String> feedUrls = this.getFeedUrls();

        if(feedUrls.size() != 0) {
            FeedParser feedParser = new FeedParser();

            for(String feedUrl : feedUrls) {
                for (JsonValue value : feedParser.getTexts(feedUrl, this.keywords)) {
                    JsonObject text = (JsonObject) value;
                    this.texts.add(text);
                }
            }
        }
        else {
            Spider spider = new Spider();
            for(JsonValue value : spider.search(this.source, this.keywords)) {
                JsonObject text = (JsonObject) value;
                this.texts.add(text);
            }
        }

        return;
    }

    /**
     * Takes an url and searches for available feeds and returns them.
     *
     * @return List<String> of available feeds
     */
    private List<String> getFeedUrls() {
        List<String> feedUrls = new ArrayList<>();
        try {
            URL url = new URL(this.source);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                if(line.contains("<link rel=\"alternate\"") && line.contains("type=\"application/rss+xml\"")) {
                    String possibleUrl = line.split("href=")[1].split("\"")[1];

                    if(possibleUrl.contains("http"))
                        feedUrls.add(possibleUrl);
                    else {
                        String[] splittedSource = this.source.split("/");
                        feedUrls.add(splittedSource[0] + "//" + splittedSource[2] + possibleUrl);
                    }
                }
            }

            return feedUrls;
        } catch (IOException ioe) {
            return feedUrls;
        }
    }
}
