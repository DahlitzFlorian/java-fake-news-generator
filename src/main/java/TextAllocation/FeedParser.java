package TextAllocation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.XmlReader;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
*Class for text creation. The input parameter is a keyword. The artikelBeschaffung method searches in the given rss feeds for news which are containing the keyword.
*@author Fichte
*
*/
class FeedParser implements FeedParserInterface {

    public JsonArray getTexts(String source, String[] keywords) {
        final String fullFeed = "https://www.freefullrss.com/feed.php?url=";
        final String fullFeedOptions = "&max=20&links=preserve&exc=&submit=Create+Full+Text+RSS";

        source = fullFeed + source + fullFeedOptions;

        JsonArrayBuilder articles = Json.createArrayBuilder();

        String news;
        String headline;

        try {
            URL feedUrl = new URL(source);
            SyndFeedInput feedInput = new SyndFeedInput();
            SyndFeed newsFeed = feedInput.build(new XmlReader(feedUrl));

            for(SyndEntry feedEntry : newsFeed.getEntries()) {
                news = feedEntry.getDescription().getValue();
                headline = feedEntry.getTitle();
                List<String> tags = new ArrayList<>();

                for(SyndCategory tag : feedEntry.getCategories())
                    tags.add(tag.getName().toLowerCase());

                boolean containsOne = false;
                int keywordsInArticle = 0;

                for(String keyword : keywords) {
                    if(tags.contains(keyword))
                        containsOne = true;

                    if(news.toLowerCase().contains(keyword))
                        keywordsInArticle++;
                }

                if(containsOne || keywordsInArticle + 1 == keywords.length)
                    articles.add(this.articleBuilder(feedEntry.getLink(), headline, news)); //Call method to build return JSON
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return articles.build();
    }

    private JsonObject articleBuilder(String resource, String title, String content) {
        JsonObjectBuilder article = Json.createObjectBuilder();

        JsonArrayBuilder  tags = Json.createArrayBuilder();

        article.add("resource", resource);
        article.add("title", title);
        article.add("tags", tags.build());
        article.add( "content", content);

        return article.build();
    }
}
