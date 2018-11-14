package TextSynthesis;

import Configuration.Configuration;
import TextAllocation.TextAllocation;
import TextClassification.TextClassification;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.List;

public class TextSynthesis {

    public String createArticle(List<String> keywords) {
        String statusCode = "Status: 200 - Successful.";

        Configuration config = new Configuration();
        JsonObject textConfig = config.getTextConfigurations();
        List<String> sources = config.getSources(textConfig);

        TextAllocation textAllocation = new TextAllocation();
        JsonArray unanalysedTexts = textAllocation.getTexts(keywords, sources);

        TextClassification textClassification = new TextClassification();
        JsonArray analysedTexts = textClassification.getAnalysedTexts(unanalysedTexts);

        String article = this.synthesise(analysedTexts);
        this.save(article);

        return statusCode;
    }

    private String synthesise(JsonArray analyzedTexts) { return ""; }

    private void save(String article) { return; }
}
