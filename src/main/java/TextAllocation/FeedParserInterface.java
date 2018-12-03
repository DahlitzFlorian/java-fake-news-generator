package TextAllocation;

import javax.json.JsonArray;

public interface FeedParserInterface {

    /**
     * Iterates over a given feed and searches for articles meeting the requirements specified
     * by the keywords.
     *
     * @param source Source to get articles from
     * @param keywords Required words to match users input
     * @return JsonArray of articles as JsonObjects
     */
    JsonArray getTexts(String source, String[] keywords);
}
