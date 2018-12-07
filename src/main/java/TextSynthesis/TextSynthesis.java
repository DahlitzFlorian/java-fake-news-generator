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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * Class-based representation of the text synthesis component
 *
 * @author Dahlitz
 * @reviewed Leuschner
 */
public class TextSynthesis {

    private static final String CONFIG_PATH = "config.json";
    private static final int MARKOV_ORDER = 3;

    public enum StatusCodes {
        SUCCESS("2000 - Successful"),
        FAILED_ON_CONFIGURATION("3000 - Failed to load configuration"),
        FAILED_ON_IMAGE_ALLOCATION("4000 - Failed to download an image for the article"),
        FAILED_ON_ARTICLE_PATH("4100 - Failed to find the path to the article"),
        FAILED_ON_IMAGE_URL("4200 - Failed to access the images url"),
        FAILED_ON_ARTICLE_GENERATION("5000 - Failed to generate the article: Check your settings"),
        FAILED_ON_ANALYSED_TEXTS("6000 - Failed to access the analysed texts"),
        FAILED_ON_ALLOCATING_TEXTS("7000 - Failed allocating articels: Use different keywords!");

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
    public String createArticle(String[] keywords, boolean withImage) {

        Configuration config = new Configuration();
        JsonObject textConfig;

        if (Files.exists(Paths.get(CONFIG_PATH))) {
            try {
                textConfig = config.getTextConfigurations();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return StatusCodes.FAILED_ON_CONFIGURATION.getCode();
            }
        } else {
            return StatusCodes.FAILED_ON_CONFIGURATION.getCode();
        }


        List<String> sources = config.getSources(textConfig);

        TextAllocation textAllocation = new TextAllocation();
        JsonArray unanalysedTexts = textAllocation.getTexts(keywords, sources);

        TextClassification textClassification = new TextClassification();
        JsonArray analysedTexts = textClassification.getAnalysedTexts(unanalysedTexts);

        String[] result = this.synthesise(analysedTexts);
        if (result[0].equals(StatusCodes.FAILED_ON_CONFIGURATION.getCode()) || result[0].equals(StatusCodes.FAILED_ON_ALLOCATING_TEXTS.getCode())) {
            return result[0];
        }
        String pathToArticle = save(result[0], result[1]);

        if (withImage) {
            ImageAllocation imageAllocation = new ImageAllocation();
            String status = imageAllocation.getImage(pathToArticle, keywords);
            if (status.equals(""))
                return StatusCodes.SUCCESS.getCode();
            else
                return status;
        }
        return StatusCodes.SUCCESS.getCode();
    }

    /**
     * Handling the control flow for synthesising a new article.
     *
     * @param analyzedTexts Texts on which basis the new article is synthesised
     * @return String[] representing [0] headline and [1] content
     */
    private String[] synthesise(JsonArray analyzedTexts) {
        Configuration configuration = new Configuration();
        StringBuilder keyWordsBuilder = new StringBuilder();
        StringBuilder corpusBuilder = new StringBuilder();
        Random random = new Random();


        int length;
        int articleCount = analyzedTexts.size();
        if (articleCount == 0) return new String[]{StatusCodes.FAILED_ON_ALLOCATING_TEXTS.getCode(), ""};
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
            titles[i++] = title;

            for (JsonValue tag : tags) {
                String tagAsString = tag.toString();
                tagAsString = tagAsString.replaceAll("\"", "");
                keyWordsBuilder.append(tagAsString);
                keyWordsBuilder.append(",");
            }
            corpusBuilder.append(corpus);
            corpusBuilder.append("\n");
        }
        keywords = keyWordsBuilder.toString().split(",");

        try {
            length = configuration.getTextConfigurations().getInt("max");
        } catch (IOException e) {
            String[] status = {StatusCodes.FAILED_ON_CONFIGURATION.getCode(), ""};
            return status;
        }

        MarkovChain markovChain = new MarkovChain(corpusBuilder.toString(), MARKOV_ORDER, length, keywords);
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

        if (headline.equals(StatusCodes.FAILED_ON_ANALYSED_TEXTS.getCode()))
            return StatusCodes.FAILED_ON_ANALYSED_TEXTS.getCode();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
        String directory = headline.replaceAll("[\"?<>*:/|]", "") + LocalDateTime.now().format(formatter);
        File finalDirectory = new File(directory);
        finalDirectory.mkdir();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + "/" + headline.replaceAll("[\"?<>*:/|]", "") + ".txt"))) {
            writer.write(headline);
            writer.newLine();
            writer.newLine();
            writer.write(content);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return finalDirectory.getAbsolutePath();
    }
}
