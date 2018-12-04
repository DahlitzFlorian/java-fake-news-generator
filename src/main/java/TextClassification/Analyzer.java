package TextClassification;

/**
 * Interface for a text analyzer
 *
 * @author Huber
 * @reviewed Dahlitz
 */
public interface Analyzer {
	String JSON_FILE = "file.json";

	int getEmptyParagraphs(String[] keywords);
	int searchKeywordsAndLines(String[] keywords);
}
