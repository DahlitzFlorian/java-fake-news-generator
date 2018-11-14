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

    public JsonObject articleCollector(String keyword) {
        JsonObjectBuilder articleJSON = Json.createObjectBuilder();
        ArrayList<String> urls = new ArrayList<String>();
        urls.add("https://www.freefullrss.com/feed.php?url=http%3A%2F%2Fwww.spiegel.de%2Fwirtschaft%2Findex.rss&max=10&links=preserve&exc=&submit=Create+Full+Text+RSS");
        urls.add("https://www.freefullrss.com/feed.php?url=http%3A%2F%2Fwww.tagesschau.de%2Fxml%2Frss2&max=5&links=preserve&exc=&submit=Create+Full+Text+RSS");
        urls.add("https://www.freefullrss.com/feed.php?url=http%3A%2F%2Frss.sueddeutsche.de%2Frss%2FTopthemen&max=5&links=preserve&exc=&submit=Create+Full+Text+RSS");

        String news;
        String headline;

        for(Iterator u = urls.iterator(); u.hasNext();) {
            try {
                URL feedUrl = new URL(u.next().toString());
                SyndFeedInput feedInput = new SyndFeedInput(); //create a Feed input
                SyndFeed newsFeed = feedInput.build(new XmlReader(feedUrl)); //Build reader for the RSSFeed

                for(Iterator i = newsFeed.getEntries().iterator(); i.hasNext(); ) {
                    SyndEntry referenceArticle = (SyndEntry) i.next(); //Every Feed entry is one Article
                    news = referenceArticle.getDescription().toString(); //Receiving the reference article
                    headline = referenceArticle.getTitle(); //Receiving the headling

                    if(news.contains(keyword)) {
                        articleJSON = this.articleJSONBuilder(articleJSON, news, headline); //Call method to build return JSON
                        System.out.print("Array is " + articleJSON.build().toString());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error" + e.getMessage());
            }
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
