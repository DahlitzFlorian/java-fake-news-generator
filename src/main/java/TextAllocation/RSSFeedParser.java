package TextAllocation;

import java.net.URL;
import java.util.*;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.XmlReader;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/*
Class for text creation. The input parameter is a keyword. The artikelBeschaffung method searches in the given rss feeds for news which are containing the keyword.


*/
class RSSFeedParser implements RSSFeedParserInterface {

    public JsonObject articleCollector(String source, List<String> keywords) {
        JsonObjectBuilder articleJSON = Json.createObjectBuilder();

        String news;
        String headline;

        try {
            URL feedUrl = new URL(source);
            SyndFeedInput feedInput = new SyndFeedInput(); //create a Feed input
            SyndFeed newsFeed = feedInput.build(new XmlReader(feedUrl)); //Build reader for the RSSFeed

            for(SyndEntry feedEntry : newsFeed.getEntries()) {
                news = feedEntry.getDescription().toString(); //Receiving the reference article
                headline = feedEntry.getTitle(); //Receiving the headling

                boolean containsAll = true;
                for(String keyword : keywords) {
                    if (!news.contains(keyword)) {
                        containsAll = false;
                        break;
                    }
                }

                if(containsAll)
                    articleJSON = this.articleJSONBuilder(articleJSON, news, headline); //Call method to build return JSON
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return articleJSON.build();
    }

    private String articleCutter(String article) { //class to cut the meta information from the reference articles
        article = article.replaceAll("SyndContentImpl.*" , "");
        article = article.replaceAll("\\<.*?>","");

        return article;
    }

    private JsonObjectBuilder articleJSONBuilder(JsonObjectBuilder articleJson, String news, String title) { // Input are a JSON article, the news, and the headline
        RSSFeedParser cutter = new RSSFeedParser();

        JsonObjectBuilder paragraphObject = Json.createObjectBuilder();
        JsonArrayBuilder paragraphArray = Json.createArrayBuilder();
        JsonArrayBuilder  tags = Json.createArrayBuilder(); //empty JSON array for tags

        news = cutter.articleCutter(news); // first cutting out all meta information
        paragraphObject.add("tags", tags); //tags are added in the Textanalyse
        paragraphObject.add("content", "");
        paragraphArray.add(paragraphObject);

        articleJson.add("ressource", news); //each reference article is a ressource
        articleJson.add("title", title);
        articleJson.add( "paragraph", paragraphArray);

        return articleJson;
    }
}
