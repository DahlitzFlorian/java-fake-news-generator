package TextAllocation;

import javax.json.JsonArray;
import java.util.List;

public interface FeedParserInterface {

    JsonArray getTexts(String source, List<String> keywords);
}
