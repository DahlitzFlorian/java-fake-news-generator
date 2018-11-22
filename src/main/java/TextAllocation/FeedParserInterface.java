package TextAllocation;

import javax.json.JsonArray;

public interface FeedParserInterface {

    JsonArray getTexts(String source, String[] keywords);
}
