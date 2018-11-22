package TextClassification;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextClassification {

    public List<String> getAnalysedTexts(JsonArray unanalysedTexts) {
        TextAnalyzer textAnalyzer = new TextAnalyzer();
        Map<String, Integer> mosteFrequentNominals = textAnalyzer.getMostFrequentlyNominals(textAnalyzer.searchNominal(unanalysedTexts));
        Map<String, Double> keywords = textAnalyzer.TFIDF(textAnalyzer.buildWordOccurrenceArrayList(unanalysedTexts));
        Map<String, Double> mostFrequentlyUsed =
                keywords.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(5) //magic number
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        List<String> tags = new ArrayList<>();
        for (String s : mosteFrequentNominals.keySet()) {
            tags.add(s + ",");
        }
        for (String s : mostFrequentlyUsed.keySet()) {
            tags.add(s + ",");
        }

        List<String> paragraphs = new ArrayList<>();
        List<String> paragraphsFromJson = getParagraphsFromJson(unanalysedTexts);

        for (String tag : tags) {
            for (String paragraph : paragraphsFromJson) {
                if (paragraph.contains(tag)) {
                    paragraphs.add(paragraph);
                }
            }
        }

        return paragraphs;
    }

    private List<String> getParagraphsFromJson(JsonArray article) {
        //TODO make it better
        int arrayLength = article.size() / 3; //magic number -> array somehow contains 3 ??
        List<String> paragraphs = new ArrayList<>();
        for (int i = 0; i < arrayLength; i++) {
            JsonArray temp = article.getJsonObject(i).getJsonArray("paragraph");
            int paragraphArrayLength = temp.size();
            for (int j = 0; j < paragraphArrayLength; j++) {
                paragraphs.add(temp.getJsonObject(j).getString("content"));
            }
        }
        return paragraphs;
    }
}
