package TextClassification;

/**
 * @author Huber
 */

public interface Analyzer {
	final String JSON_FILE = "file.json";

	public int getEmptyParagraphs();

	public int searchKeywordsAndLines(String words[]);
}
