package TextClassification;

/**
 * @author Huber
 */

public interface Analyzer {
	String JSON_FILE = "file.json";

	int getEmptyParagraphs(String[] keywords);
	int searchKeywordsAndLines(String[] keywords);
}
