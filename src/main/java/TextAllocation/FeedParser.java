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
 * Class-based representation of a feed parser
 *
 * @author Fichte
 * @reviewed Dahlitz
 */
class FeedParser implements FeedParserInterface {

    /**
     * Iterates over a given feed and searches for articles meeting the requirements specified
     * by the keywords. An article meets the requirements if at least one keyword is part of the
     * articles categories or if the articles content contains at least n - 1 keywords.
     *
     * @param source Source to get articles from
     * @param keywords Required words to match users input
     * @return JsonArray of articles as JsonObjects
     */
    public JsonArray getTexts(String source, String[] keywords) {
        final String fullFeed = "https://www.freefullrss.com/feed.php?url=";
        final String fullFeedOptions = "&max=50&links=preserve&exc=&submit=Create+Full+Text+RSS";

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
                    articles.add(this.articleBuilder(feedEntry.getLink(), headline, news));
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return articles.build();
    }

    /**
     * Builds an article object meeting the project requirements.
     *
     * @param resource Url of the original article
     * @param title String representing the articles title
     * @param content JsonArray containing the contents paragraphs
     * @return JsonObject representing an article
     */
    private JsonObject articleBuilder(String resource, String title, String content) {
        JsonObjectBuilder article = Json.createObjectBuilder();
        JsonArrayBuilder  tags = Json.createArrayBuilder();

        article.add("resource", resource);
        article.add("title", title);
        article.add("tags", tags.build());
        article.add( "content", content.replaceAll("\\<.*?>",""));

        return article.build();
    }
}
