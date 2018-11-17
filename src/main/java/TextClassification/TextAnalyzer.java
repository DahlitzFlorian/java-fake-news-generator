package TextClassification;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.json.JsonArrayBuilder;

import javax.json.JsonArray;
import javax.json.Json;
import javax.json.JsonObjectBuilder;


import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * A class for analyzing a text (e.g. counting keywords and to find the lines,
 * where they're)
 *
 * @author Huber
 */

class TextAnalyzer implements Analyzer {
	JsonArray mJsonArray;

	public int getEmptyParagraphs(String words[]) {
		String line;
		int emptyLine = 0;

		try {
			// Create a reader which reads json-files
			InputStream fileInputStream = new FileInputStream(JSON_FILE);
			JsonReader jsonReader = Json.createReader(fileInputStream);
			mJsonArray = jsonReader.readArray();

			int emptyParagraphCounter = 0;
			int lineNumber = 0;
			while ((line = ((BufferedReader) mJsonArray).readLine()) != null) {
				if (line.equals("")) {
					lineNumber++;
					emptyParagraphCounter++;
					System.out.println("The" + emptyParagraphCounter + "empty paragraph is in line" + lineNumber);
					emptyLine = lineNumber;
				} else if (!(line.equals(""))) {
					lineNumber++;
				}
			}

			// Close jsonReader and fileInputStream
			jsonReader.close();
			fileInputStream.close();

		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return emptyLine;
	}

	public int searchKeywordsAndLines(String words[]) {
		String fileLine = "";
		String searchedWord = words[0];

		int lineNumber = 0;
		int countWord = 0;

		if (words.length > 0) {

			try {
				// Create a reader which reads json-files
				InputStream fileInputStream = new FileInputStream(JSON_FILE);
				JsonReader jsonReader = Json.createReader(fileInputStream);
				mJsonArray = jsonReader.readArray();

				// Close jsonReader and fileInputStream
				jsonReader.close();
				fileInputStream.close();

				// Search position and how often this words appear in this text
				while ((fileLine = ((BufferedReader) mJsonArray).readLine()) != null) {
					lineNumber++;
					int position = fileLine.indexOf(searchedWord);

					if (position > -1) {
						countWord++;
						System.out.println("The word is at " + position + ", line " + lineNumber);
					}
				}

				// Close BufferedReader
				((BufferedReader) mJsonArray).close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		} else {
			System.out.println("Please enter a word.");
		}

		return countWord;
	}


	/**
	 * Method to count Nominals. Nominals are recognized by regex. If a string contains a Nominal at least twice, the Nominal
	 * is counted by using a JSON.
	 *
	 * @author Fichte
	 */

	public class testAnalyzer {
		public Map<String, Integer> searchNominal(JsonArray article) {
			String line;
			int nu = 1;
			ArrayList<String> nominal = new ArrayList<String>();
			Map<String, Integer> nominalCounter = new HashMap<String, Integer>();
			int tempNumber;
			String news;
			String[] newsArray;
			news = article.toString();
			news = news.replaceAll("/\n/g,", " ");
			newsArray = news.split(" ");
			for (int i = 0; i < newsArray.length; i++) {
				String tempword = newsArray[i].toString();
				if (tempword.matches("^[A-Z](.*)") && nominal.contains(tempword)) {
					tempNumber = nominalCounter.get(tempword) + 1;
					nominalCounter.put(tempword, tempNumber);
				} else if (tempword.matches("^[A-Z](.*)")) {
					nominal.add(tempword);
					nominalCounter.put(tempword, 1);

				}


			}

			for (String name : nominal) {
				System.out.println(name);
			}

			return nominalCounter;
		}
	}
}
