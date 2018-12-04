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

        TextAllocation textAllocation = new TextAllocation();
        TextClassification textClassification = new TextClassification();
        ImageAllocation imageAllocation = new ImageAllocation();
        CompletableFuture<String> status = new CompletableFuture<>();

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> config.getSources(textConfig))
                .thenApply((sources) -> textAllocation.getTexts(keywords, sources))
                .thenApply(textClassification::getAnalysedTexts)
                .thenApply(this::synthesise)
                .thenApply(this::save)
                .thenCompose((path) -> imageAllocation.getImage(path, keywords));

        try {
            status.complete(future.get());

            if(status.isDone()) {
                if (status.get().equals(""))
                    return StatusCodes.SUCCESS.getCode();
                else if(status.get().equals(StatusCodes.FAILED_ON_ANALYSED_TEXTS.getCode()))
                    return StatusCodes.FAILED_ON_ANALYSED_TEXTS.getCode();
            }
        } catch (InterruptedException | ExecutionException ee) {
            System.out.println("Error: " + ee.getMessage());
            return StatusCodes.FAILED_ON_ARTICLE_GENERATION.getCode();
        }



        return StatusCodes.FAILED_ON_ARTICLE_GENERATION.getCode();
    }

    private CompletableFuture<String[]> synthesise(CompletableFuture<JsonArray> analyzedTexts) {
        JsonArray completedAnalyzedTexts;

        try {
            completedAnalyzedTexts = analyzedTexts.get();
        } catch(InterruptedException | ExecutionException ee) {
            System.out.println("Error: " + ee.getMessage());
            String[] code = {StatusCodes.FAILED_ON_ANALYSED_TEXTS.getCode(), ""};
            return CompletableFuture.completedFuture(code);
        }

        Configuration configuration = new Configuration();
        StringBuilder corpusBuilder = new StringBuilder();
        StringBuilder keyWordsBuilder = new StringBuilder();
        Random random = new Random();

        int length;
        int articleCount = completedAnalyzedTexts.size();
        int randomTitleNumber = random.nextInt(articleCount);
        int i = 0;

        String corpus;
        String[] keywords;
        String[] titles = new String[articleCount];

        for (JsonValue articleJson : completedAnalyzedTexts) {
            JsonObject article = articleJson.asJsonObject();
            JsonArray content = article.getJsonArray("content");
            JsonArray tags = article.getJsonArray("tags");
            String title = article.getString("title");
            titles[i++] = title.replaceAll("[\"?<>*:/|]", "");

            for (JsonValue paragraph : content) {
                String paragraphAsString = paragraph.toString();
                paragraphAsString = paragraphAsString.substring(1,paragraphAsString.length()-1);
                corpusBuilder.append(paragraphAsString);
                corpusBuilder.append(" ");
            }

            for (JsonValue tag : tags) {
                String tagAsString = tag.toString();
                tagAsString = tagAsString.replaceAll("\"", "");
                keyWordsBuilder.append(tagAsString);
                keyWordsBuilder.append(",");
            }
        }

        corpus = corpusBuilder.toString();
        keywords = keyWordsBuilder.toString().split(",");

        try {
            length = configuration.getTextConfigurations().getInt("max");
        } catch (IOException e) {
            return CompletableFuture.completedFuture(null);
        }

        MarkovChain markovChain = new MarkovChain(corpus, 3, length, keywords);
        String[] result = {titles[randomTitleNumber], markovChain.markovify()};

        return CompletableFuture.completedFuture(result);
    }

    /**
     * Saves an article as a text-file to a newly created directory in the softwares root directory.
     *
     * @param content String[] containing the articles headline and content
     * @return String representing the path to the articles directory
     */
    private CompletableFuture<String> save(CompletableFuture<String[]> content) {
        String headline, article;

        try {
            headline = content.get()[0];
            article = content.get()[1];
        } catch(InterruptedException | ExecutionException ee) {
            return CompletableFuture.completedFuture("");
        }

        if(headline.equals(StatusCodes.FAILED_ON_ANALYSED_TEXTS.getCode()))
            return CompletableFuture.completedFuture(StatusCodes.FAILED_ON_ANALYSED_TEXTS.getCode());

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

        return CompletableFuture.completedFuture(finalDirectory.getAbsolutePath());
    }


}
