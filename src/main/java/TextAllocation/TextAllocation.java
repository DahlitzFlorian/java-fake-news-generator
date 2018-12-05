package TextAllocation;

import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Class-based representation of the text allocation component
 *
 * @author Dahlitz
 */
public class TextAllocation {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        methodToTime();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        return;
    }

    public static void methodToTime() {
        TextAllocation textAllocation = new TextAllocation();
        String[] keywords = {"politik", "usa"};
        List<String> sources = new ArrayList<>();
        sources.add("http://spiegel.de");
        sources.add("https://bild.de");
        sources.add("https://www.tagesschau.de/");
        sources.add("https://www.welt.de/");
        sources.add("https://www.faz.net/aktuell/");
        sources.add("https://www.sueddeutsche.de/");
        sources.add("https://de.sputniknews.com/");
        sources.add("https://www.zeit.de/index");
        JsonArray jsonArray = textAllocation.getTexts(keywords, sources);

        return;
    }

    /**
     * Looks for available feeds and uses them to find articles containing the specified keywords
     * of those, which have been categorised with least one keyword. If no feed is available,
     * spider and crawler are used.
     *
     * @param keywords Keywords an articles has to be categorised as or contain
     * @param sources Sources to be searched
     * @return JsonArray containing articles as JsonObjects
     */
    public JsonArray getTexts(String[] keywords, List<String> sources) {
        JsonArrayBuilder texts = Json.createArrayBuilder();

        ExecutorService executorService = Executors.newCachedThreadPool();

        for(String source : sources)
            executorService.execute(new TextAllocationThread(source, keywords, texts));

        executorService.shutdown();

        while(!executorService.isTerminated()) {

        }

        return texts.build();
    }
}
