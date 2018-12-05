package TextSynthesis;

import Configuration.Configuration;
import ImageAllocation.ImageAllocation;
import TextAllocation.TextAllocation;
import TextClassification.TextClassification;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Class-based representation of the text synthesis component
 *
 * @author Dahlitz
 * @reviewed Leuschner
 */
public class TextSynthesis {

    public enum StatusCodes {
        SUCCESS("2000 - Successful"),
        FAILED_ON_CONFIGURATION("3000 - Failed to load configuration"),
        FAILED_ON_IMAGE_ALLOCATION("4000 - Failed to download an image for the article"),
        FAILED_ON_ARTICLE_PATH("4100 - Failed to find the path to the article"),
        FAILED_ON_IMAGE_URL("4200 - Failed to access the images url"),
        FAILED_ON_ARTICLE_GENERATION("5000 - Failed to generate the article: Check your settings"),
        FAILED_ON_ANALYSED_TEXTS("6000 - Failed to access the analysed texts");

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
        JsonObject textConfig;

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
        String pathToArticle = save(result[0], result[1]);

        ImageAllocation imageAllocation = new ImageAllocation();
        String status = imageAllocation.getImage(pathToArticle, keywords);

        if(status.equals(""))
            return StatusCodes.SUCCESS.getCode();
        else
            return status;
    }

    private String[] synthesise(JsonArray analyzedTexts) {

        Configuration configuration = new Configuration();
        StringBuilder keyWordsBuilder = new StringBuilder();
        Random random = new Random();

        int length;
        int articleCount = analyzedTexts.size();
        int randomTitleNumber = random.nextInt(articleCount);
        int i = 0;

        String corpus = "";
        String[] keywords;
        String[] titles = new String[articleCount];

        for (JsonValue articleJson : analyzedTexts) {
            JsonObject article = articleJson.asJsonObject();
            corpus = article.getString("content");
            JsonArray tags = article.getJsonArray("tags");
            String title = article.getString("title");
            titles[i++] = title.replaceAll("[\"?<>*:/|]", "");

            for (JsonValue tag : tags) {
                String tagAsString = tag.toString();
                tagAsString = tagAsString.replaceAll("\"", "");
                keyWordsBuilder.append(tagAsString);
                keyWordsBuilder.append(",");
            }
        }
        keywords = keyWordsBuilder.toString().split(",");

        try {
            length = configuration.getTextConfigurations().getInt("max");
        } catch (IOException e) {
            String[] status = {StatusCodes.FAILED_ON_CONFIGURATION.getCode(), ""};
            return status;
        }

        MarkovChain markovChain = new MarkovChain(corpus, 2, length, keywords);
        String[] result = {titles[randomTitleNumber], markovChain.markovify()};

        return result;
    }

    /**
     * Saves an article as a text-file to a newly created directory in the softwares root directory.
     *
     * @param content String[] containing the articles headline and content
     * @return String representing the path to the articles directory
     */
    private String save(String headline, String content) {

        if(headline.equals(StatusCodes.FAILED_ON_ANALYSED_TEXTS.getCode()))
            return StatusCodes.FAILED_ON_ANALYSED_TEXTS.getCode();

        File finalDirectory = new File(headline);
        finalDirectory.mkdir();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(headline + "/" + headline + ".txt"))) {
            writer.write(headline);
            writer.newLine();
            writer.newLine();
            writer.write(content);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        return finalDirectory.getAbsolutePath();
    }


}
