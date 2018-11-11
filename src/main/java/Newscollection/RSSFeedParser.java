
import java.net.URL;
import java.util.*;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.XmlReader;
//import org.json.JSONObject;
import org.json.*;

/*
Class for text creation. The input parameter is a keyword. The artikelBeschaffung method searches in the given rss feeds for news which are containing the keyword.


*/
public class RSSFeedParser implements RSSFeedParserInterface {


    public JSONObject articleCollector(String keyword) {
        JSONObject articleJSON = new JSONObject();
        ArrayList<String> urls = new ArrayList<String>();
        urls.add("https://www.freefullrss.com/feed.php?url=http%3A%2F%2Fwww.spiegel.de%2Fwirtschaft%2Findex.rss&max=10&links=preserve&exc=&submit=Create+Full+Text+RSS");
        urls.add("https://www.freefullrss.com/feed.php?url=http%3A%2F%2Fwww.tagesschau.de%2Fxml%2Frss2&max=5&links=preserve&exc=&submit=Create+Full+Text+RSS");
        urls.add("https://www.freefullrss.com/feed.php?url=http%3A%2F%2Frss.sueddeutsche.de%2Frss%2FTopthemen&max=5&links=preserve&exc=&submit=Create+Full+Text+RSS");

        String news;
        String headline;
        for (Iterator u = urls.iterator(); u.hasNext();) {
            try {

                URL feedUrl = new URL(u.next().toString());
                SyndFeedInput feedinput = new SyndFeedInput(); //create a Feed input
                SyndFeed newsFeed = feedinput.build(new XmlReader(feedUrl)); //Build reader for the RSSFeed

                for (Iterator i = newsFeed.getEntries().iterator(); i.hasNext(); ) {
                    SyndEntry referenceArticle = (SyndEntry) i.next(); //Every Feed entry is one Article
                    news = referenceArticle.getDescription().toString(); //Receiving the reference article
                    headline = referenceArticle.getTitle().toString(); //Receiving the headling

                    if (news.contains(keyword) == true) {
                        RSSFeedParser JSONbuilder = new RSSFeedParser();
                        articleJSON = JSONbuilder.articleJSONBuilder(articleJSON, news, headline); //Call method to build return JSON
                        System.out.print("Array is" + articleJSON);
                    }

                }


            } catch (Exception e) {
                System.out.println("Error" + e.getMessage());
            }



        }
        return articleJSON;
    }

    public String articleCutter(String article){ //class to cut the meta information from the reference articles
        article = article.replaceAll("SyndContentImpl.*" , "");
        article = article.replaceAll("\\<.*?>","");
        return article;
    }

    public JSONObject articleJSONBuilder(JSONObject articleJson, String News, String title){ // Input are a JSON article, the news, and the headline
        RSSFeedParser cutter = new RSSFeedParser();
        News = cutter.articleCutter(News); // first cutting out all meta information
        articleJson.put("ressource", News); //each reference article is a ressource
        articleJson.put("title", title);
        JSONObject paragraphObject = new JSONObject();
        JSONArray paragraphArray = new JSONArray();
        JSONArray  tags = new JSONArray(); //empty JSON array for tags
        paragraphObject.put("tags", tags); //tags are added in the Textanalyse
        paragraphObject.put("content", "");
        paragraphArray.put(paragraphObject);
        articleJson.put( "paragraph", paragraphArray);
        return articleJson;
    }
}
