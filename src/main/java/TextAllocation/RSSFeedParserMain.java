package TextAllocation;

import javax.json.JsonObject;

public class RSSFeedParserMain {
    public static void main(String[] args) {
        String key = "Jahr";
      RSSFeedParser Test = new RSSFeedParser();
        JsonObject jsonObject = Test.articleCollector(key);

        System.out.println(jsonObject.toString());

    }
}
