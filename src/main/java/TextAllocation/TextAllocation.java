package TextAllocation;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class-based representation of the text allocation component
 *
 * @author Dahlitz
 */
public class TextAllocation {

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
