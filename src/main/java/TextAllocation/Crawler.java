package TextAllocation;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Crawler {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;

    // Give it a URL and it makes an HTTP request for a web page
    public boolean crawl(String url) {

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

    // Tries to find a word on the page
    public boolean searchForWord(List<String> keywords) {
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }

        String bodyText = this.htmlDocument.body().text();

        for(String keyword : keywords)
            if(!bodyText.toLowerCase().contains(keyword.toLowerCase()))
                return false;

        return true;
    }

    // Returns a list of all the URLs on the page
    public List<String> getLinks() {
        return this.links;
    }
}
