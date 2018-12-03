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

/**
 * Class-based representation of the text synthesis component
 *
 * @author Dahlitz
 */
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

    /**
     * Handles the main control flow of the whole software.
     *
     * @param keywords Keywords the generated article can be classified by
     * @return String representing a status code
     */
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

    /**
     * Saves an article as a text-file to a newly created directory in the softwares root directory.
     *
     * @param headline Articles title
     * @param article Articles content
     * @return String representing the path to the articles directory
     */
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
