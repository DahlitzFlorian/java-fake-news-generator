package TextClassification;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextClassification {
    private static final int TAGS_COUNT = 5;

    public JsonArray getAnalysedTexts(JsonArray unanalysedTexts) {
        System.out.println(unanalysedTexts.toString());
        JsonArrayBuilder analyzedJson = Json.createArrayBuilder();
        TextAnalyzer textAnalyzer = new TextAnalyzer();
        List<String> trainingTexts = textAnalyzer.getTFIDFFillerTexts();
        List<Map<String, Integer>> tfidfTexts = new ArrayList<>();
        for(String trainingText: trainingTexts) {
            tfidfTexts.add(textAnalyzer.wordOccurrence(textAnalyzer.splitWords(trainingText, true)));
        }


        for (JsonValue articleJson : unanalysedTexts) {
            int i = 0;
            JsonArray paragraphs = articleJson.asJsonObject().getJsonArray("paragraph");
            JsonObjectBuilder analyzedArticle = Json.createObjectBuilder(articleJson.asJsonObject());
            JsonArrayBuilder analyzedParagraphs = Json.createArrayBuilder(paragraphs);
            for (JsonValue paragraph : paragraphs) {
                String[] words = textAnalyzer.splitWords(paragraph.toString(), true);
                tfidfTexts.add(0, textAnalyzer.wordOccurrence(words));
                Map<String, Double> tagsMap = textAnalyzer.TFIDF(tfidfTexts).entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(TAGS_COUNT)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                JsonArrayBuilder tags = Json.createArrayBuilder();
                for(String tag : tagsMap.keySet()) {
                    tags.add(tag);
                }
                analyzedParagraphs.set(i++, tags);
            }
            tfidfTexts.remove(0);
            analyzedArticle.remove("paragraph");
            analyzedArticle.add("paragraph", analyzedParagraphs);
            analyzedJson.add(analyzedArticle);
        }

        return analyzedJson.build();
    }
}
