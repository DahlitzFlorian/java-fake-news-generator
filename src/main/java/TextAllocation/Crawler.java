package TextAllocation;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class-based representation of a crawler
 *
 * @author Dahlitz
 */
public class Crawler {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<>();
    private Document htmlDocument;

    /**
     * Takes a url and searches for further useful links and adds them to the queue if existing.
     *
     * @param url Search for further links
     * @return True if further links were found
     */
    boolean crawl(String url) {

        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;

            if(connection.response().statusCode() == 200) {
                System.out.println("\n**Visiting** Received web page at " + url);
            }

            if(!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }

            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");

            for(Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }

            return true;
        } catch(IOException ioe) {
            return false;
        }
    }

    /**
     * Searches for keywords on the web pages.
     *
     * @param keywords Keywords to search for
     * @return True if the web page contains the specified keywords
     */
    boolean searchForWord(String[] keywords) {
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }

        String bodyText = this.htmlDocument.text();

        for(String keyword : keywords)
            if(!bodyText.toLowerCase().contains(keyword.toLowerCase()))
                return false;

        return true;
    }

    /**
     * Returns all the links, which were found.
     *
     * @return List<String> of links that were found
     */
    List<String> getLinks() {
        return this.links;
    }
}
