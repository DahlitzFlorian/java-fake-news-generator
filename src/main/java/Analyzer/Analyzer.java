package Analyzer;

import javax.json.JsonArray;

/**
 * @author Huber
 */

public interface Analyzer {
	final String JSON_FILE = "file.json";

	public int getEmptyParagraphs(String words[]);

	public int searchKeywordsAndLines(String words[]);
}
