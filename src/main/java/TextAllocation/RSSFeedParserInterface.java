package TextAllocation;

import javax.json.JsonArray;
import java.util.List;

public interface RSSFeedParserInterface {

    JsonArray articleCollector(String source, List<String> keywords);
}
