package TextSynthesis;

import Configuration.Configuration;
import ImageAllocation.ImageAllocation;
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
    private enum StatusCodes {
        SUCCESS("2000 - Successful"),
        FAILED_ON_CONFIGURATION("3000 - Failed to load configuration");

        private final String text;

        StatusCodes(final String text) {
            this.text = text;
        }

        public String getCode() {
            return text;
        }
    }

    public String createArticle(String[] keywords) {

        Configuration config = new Configuration();
        JsonObject textConfig = null;

        try {
            textConfig = config.getTextConfigurations();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return StatusCodes.FAILED_ON_CONFIGURATION.getCode();
        }

        List<String> sources = config.getSources(textConfig);

        TextAllocation textAllocation = new TextAllocation();
        JsonArray unanalysedTexts = textAllocation.getTexts(keywords, sources);

        TextClassification textClassification = new TextClassification();
        JsonArray analysedTexts = textClassification.getAnalysedTexts(unanalysedTexts);

        String[] result = this.synthesise(analysedTexts);
        String finalDirectory = this.save(result[0], result[1]);

        ImageAllocation imageAllocation = new ImageAllocation();
        imageAllocation.getImage(finalDirectory, keywords);

        return StatusCodes.SUCCESS.getCode();
    }

    private String[] synthesise(JsonArray analyzedTexts) {
        String[] result = {"", ""};

        return result;
    }

    public static void main(String[] args) {
        TextSynthesis textSynthesis = new TextSynthesis();
        String[] keywords = {"Politik", "usa"};
        textSynthesis.createArticle(keywords);
    }

    public String save(String headline, String article) {
        File finalDirectory = new File(headline);
        finalDirectory.mkdir();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(headline + "/" + headline + ".txt"))) {
            writer.write(headline);
            writer.newLine();
            writer.newLine();
            writer.write(article);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        return finalDirectory.getAbsolutePath();
    }


}
