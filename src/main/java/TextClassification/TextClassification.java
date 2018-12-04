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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TextClassification {
    private static final int TAGS_COUNT = 5;

    public CompletableFuture<JsonArray> getAnalysedTexts(JsonArray unanalysedTexts) {
        JsonArrayBuilder analyzedJson = Json.createArrayBuilder();
        TextAnalyzer textAnalyzer = new TextAnalyzer();
        List<String> trainingTexts = textAnalyzer.getTFIDFFillerTexts();
        List<Map<String, Integer>> tfidfTexts = new ArrayList<>();
        for(String trainingText: trainingTexts) {
            tfidfTexts.add(textAnalyzer.wordOccurrence(textAnalyzer.splitWords(trainingText, true)));
        }

        for (JsonValue articleJson : unanalysedTexts) {
            JsonArray content = articleJson.asJsonObject().getJsonArray("content");
            JsonObjectBuilder analyzedArticle = Json.createObjectBuilder(articleJson.asJsonObject());
            StringBuilder stringBuilder = new StringBuilder();
            for (JsonValue paragraph : content) {
                String paragraphAsString = paragraph.toString();
                paragraphAsString = paragraphAsString.substring(1,paragraphAsString.length()-1);
                stringBuilder.append(paragraphAsString);
                stringBuilder.append(" ");
            }

            String[] words = textAnalyzer.splitWords(stringBuilder.toString(), true);
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
        return CompletableFuture.completedFuture(analyzedJson.build());
    }



}
