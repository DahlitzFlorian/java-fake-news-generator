package TextSynthesis;

import Configuration.Configuration;
import TextAllocation.TextAllocation;
import TextClassification.TextClassification;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TextSynthesis {
    //private MarkovChain markovChain = new MarkovChain(null, 2, 500);

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

        String[] result = this.synthesise(analysedTexts);
        this.save(result[0], result[1]);

        return statuscodes[0];
    }

    private String[] synthesise(JsonArray analyzedTexts) {
        String[] result = {"", ""};

        return result;
    }

    public static void main(String[] args) {
        TextSynthesis textSynthesis = new TextSynthesis();
        textSynthesis.save("headline", "soe");
    }

    public void save(String headline, String article) {
        new File(headline).mkdir();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(headline + "/" + headline + ".txt"))) {
            writer.write(headline);
            writer.newLine();
            writer.newLine();
            writer.write(article);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }


}
