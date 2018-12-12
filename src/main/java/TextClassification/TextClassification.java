package TextClassification;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Leuschner
 */
public class TextClassification {
    private static final int TAGS_COUNT = 5;

    public JsonArray getAnalysedTexts(JsonArray unanalysedTexts) {
        JsonArrayBuilder analyzedJson = Json.createArrayBuilder();
        TextAnalyzer textAnalyzer = new TextAnalyzer();
        String trainingText = textAnalyzer.getTFIDFFillerTexts();
        List<Map<String, Integer>> tfidfTexts = new ArrayList<>();


        tfidfTexts.add(textAnalyzer.wordOccurrence(textAnalyzer.splitWords(trainingText, true)));


        for (JsonValue articleJson : unanalysedTexts) {
            String content = articleJson.asJsonObject().getString("content");
            JsonObjectBuilder analyzedArticle = Json.createObjectBuilder(articleJson.asJsonObject());

            String[] words = textAnalyzer.splitWords(content, true);
            tfidfTexts.add(0, textAnalyzer.wordOccurrence(words));
            Map<String, Double> tagsMap = textAnalyzer.TFIDF(tfidfTexts).entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(TAGS_COUNT)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            JsonArrayBuilder tags = Json.createArrayBuilder();
            for (String tag : tagsMap.keySet()) {
                tags.add(tag);
            }
            tfidfTexts.remove(0);
            analyzedArticle.remove("tags");
            analyzedArticle.add("tags", tags);
            analyzedJson.add(analyzedArticle);
        }
        return analyzedJson.build();
    }



}
