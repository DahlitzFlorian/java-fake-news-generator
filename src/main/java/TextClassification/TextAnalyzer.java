package TextClassification;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.Json;

import javax.json.JsonReader;

/**
 * A class for analyzing a text (e.g. counting keywords and to find the lines,
 * where they're)
 *
 * @author Huber
 * @reviewed Dahlitz
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
    private Map<String, Integer> searchNominal(JsonArray article) {
        Map<String, Integer> nominalCounter = new HashMap<>();
        int tempNumber;
        String[] news = article.toString().split("\\s"); //TODO get real resources
        for (String word : news) {
            if (word.matches("^[A-Z](.*)") && nominalCounter.containsKey(word)) {
                tempNumber = nominalCounter.get(word) + 1;
                nominalCounter.put(word, tempNumber);
            } else if (word.matches("^[A-Z](.*)")) {
                nominalCounter.put(word, 1);
            }
        }

        //debug feature
        for (String name : nominalCounter.keySet()) {
            System.out.println(name);
        }

        return nominalCounter;
    }


    /**
     * Method gives back the top elements with the top values. The limit is set by the ratio variable. Method filters
     * for the msot frequently used Nominals in the article
     *
     * @author Fichte
     */
    private Map<String, Integer> getMostFrequentlyNominals(Map<String, Integer> nominals) {
        Map<String, Integer> mostFrequentlyUsed;
        int amountOfElements = nominals.size();
        long ratio = Math.round(amountOfElements * 0.025);
        mostFrequentlyUsed =
                nominals.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(ratio)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return mostFrequentlyUsed;
    }


    /**
     * @param corpus      the text to be split
     * @param punctuation true removes punctuation, false just splits whitespaces
     * @return an Array containing every word of the corpus; doesn't remove duplictes
     * @author Leuschner
     * Method to split a corpus of text into each of its words either removing punctuation
     * or not
     */
    String[] splitWords(String corpus, boolean punctuation) {
        if (punctuation)
            return corpus.replaceAll("\\p{Punct}", "").split("\\s");
        else
            return corpus.split("\\s");
    }

    /**
     * @param wordList an array of the words to be counted
     * @return a Map containing the word as key and the number of occurrences as value
     * @author Leuschner
     * Method to count how many times a word occures in an array of words
     * For this method to work each entry in the array must be a single word
     */
    Map<String, Integer> wordOccurrence(String[] wordList) {
        Map<String, Integer> wordOccurrence = new HashMap<>();
        for (String word : wordList) {
            if (!wordOccurrence.containsKey(word)) {
                wordOccurrence.put(word, 1);
            } else {
                int occurence = wordOccurrence.get(word);
                wordOccurrence.put(word, ++occurence);
            }
        }
        return wordOccurrence;
    }

	Map<String, Double> TFIDF(List<Map<String, Integer>> AllWordOccurrenceMaps) {
		Map<String, Integer> firstMap = AllWordOccurrenceMaps.get(0);
		Map<String, Integer> amountItAppears= new HashMap<>();

		Map<String, Double> result = new HashMap<>();
		double amountWordsArticle1 = firstMap.size();
		double amountArticles = AllWordOccurrenceMaps.size();
		for(String word : firstMap.keySet()){
			amountItAppears.put(word, 0);
		}


		for (Map<String, Integer> Maps : AllWordOccurrenceMaps) {
			for (String word1 : Maps.keySet()) {
				if (firstMap.keySet().contains(word1)) {
					amountItAppears.put(word1, amountItAppears.get(word1)+1);
				}

			}
		}
		for(String word2 : firstMap.keySet()){
			double tempDouble = amountItAppears.get(word2);
			int amountWordInArticle1 = firstMap.get(word2);
			double denominator = amountWordInArticle1/amountWordsArticle1;
			double resultdivider = Math.log(amountArticles/tempDouble );
			double tempresult = denominator*resultdivider;
			result.put(word2, tempresult);
		}
		return result;
	}

    /**
     * @return a ArrayList containing several filler texts.
     * @author Fichte
     * Method to get filler article out of the TFIDF_Training Text directory. This directory contains several textfiles with filler material.
     */
    List<String> getTFIDFFillerTexts() {
        Scanner x;
        ArrayList<String> fillerTexts = new ArrayList<>();
        ArrayList<Integer> noDuplicates = new ArrayList<>();
        Random textIndex = new Random();
        ArrayList<String> tempText = new ArrayList<>();
        int tempTextSize = 0;
        int randomNumber;
        for (int i = 0; i < 10; i++) {
            randomNumber = textIndex.nextInt(44);
            if (!noDuplicates.contains(randomNumber)) {
                String textName = "text" + randomNumber + ".txt";
                try {

                    x = new Scanner(getClass().getClassLoader().getResourceAsStream("./training_texts/" + textName));

                } catch (Exception e) {
                    return null;
                }
                while (x.hasNext() && tempTextSize != 1000) {

                    tempText.add(x.next());
                    tempTextSize ++;

                }
                String fillerText = tempText.subList(0, tempTextSize).toString();
                tempText.clear();
                fillerText = fillerText.replaceAll(",", "");
                fillerTexts.add(fillerText);
                noDuplicates.add(randomNumber);
                tempTextSize = 0;
                x.close();
            }
            else{
                i--;
            }
        }
        return fillerTexts;
    }

}
