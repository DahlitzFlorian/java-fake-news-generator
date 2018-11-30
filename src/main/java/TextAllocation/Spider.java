package TextAllocation;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {

    private static final int MAX_PAGES = 10;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();

    /**
     * Returns the next url to visit by removing the first
     * element of the list of pages to visit for.
     * Removes duplicates automatically.
     *
     * @return Next url to visit
     */
    private String getNextUrl() {
        String nextUrl;

        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while(this.pagesToVisit.contains(nextUrl));

        this.pagesVisited.add(nextUrl);

        return nextUrl;
    }

    /**
     *
     * @param baseUrl
     * @param keywords
     */
    public JsonObject search(String baseUrl, String[] keywords) {

        JsonObjectBuilder articles = Json.createObjectBuilder();

        while(this.pagesVisited.size() < MAX_PAGES) {
            String currentUrl;
            Crawler crawler = new Crawler();

            if(this.pagesToVisit.isEmpty()) {
                currentUrl = baseUrl;
                this.pagesVisited.add(baseUrl);
            }
            else {
                currentUrl = this.getNextUrl();
            }

            crawler.crawl(currentUrl);
            boolean success = crawler.searchForWord(keywords);

            this.pagesToVisit.addAll(crawler.getLinks());
        }

        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));

        return articles.build();
    }
}
