package TextSynthesis;

import Configuration.Configuration;
import TextAllocation.TextAllocation;
import TextClassification.TextClassification;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.List;

public class TextSynthesis {

    public String createArticle(String[] keywords) {
        String[] statuscodes = {
                "Status: 2000 - Successful.",
                "Status: 3000 - Failed to load configuration."
        };

        Configuration config = new Configuration();
        JsonObject textConfig = null;

        try {
            textConfig = config.getTextConfigurations();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return statuscodes[1];
        }

        List<String> sources = config.getSources(textConfig);

        TextAllocation textAllocation = new TextAllocation();
        JsonArray unanalysedTexts = textAllocation.getTexts(keywords, sources);

        TextClassification textClassification = new TextClassification();
        JsonArray analysedTexts = textClassification.getAnalysedTexts(unanalysedTexts);

        String article = this.synthesise(analysedTexts);
        this.save(article);

        return statuscodes[0];
    }

    private String synthesise(JsonArray analyzedTexts) { return ""; }

    private void save(String article) { return; }
}
