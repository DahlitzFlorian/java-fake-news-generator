package ImageAllocation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Class-based representation of the image allocation component
 *
 * @author Huber
 * @reviewed Dahlitz
 */
public class ImageAllocation extends ArrayList<Element> {

	private static final long serialVersionUID = 1L;

    /**
     * Downloads an image and saves it to the articles directory.
     *
     * @param path Path to the articles directory
     * @param keywords Keywords an image is classified as
     */
	public void getImage(String path, String[] keywords) {
		int counter = 0;
        URL imageUrl = null;

        try {
            imageUrl = new URL(this.searchImage(String.join(" ", keywords)));
        } catch(MalformedURLException mue) {
		    System.out.println("Error: " + mue.getMessage());

		    return;
        }

		try (InputStream imageReaderInput = new BufferedInputStream(imageUrl.openStream());
             OutputStream imageWriterOutput = new BufferedOutputStream(
                     new FileOutputStream(path + File.separator + "image.jpg"))) {

			while ((counter = imageReaderInput.read()) != -1) {
				imageWriterOutput.write(counter);
			}
		} catch(IOException ioe) {
		    System.out.println("Error: " + ioe.getMessage());
        }
	}

    /**
     * Uses the google search api to return a url to an image meeting the requirements specified
     * by the keywords.
     *
     * @param keyword Keywords an image is classified as
     * @return String representing the images url
     */
	private String searchImage(String keyword) {
		int randomNumber = 10;
		String imageUrl = "";
		try {
			String url = "https://www.google.com/search?tbm=isch&q=" + keyword;
			Document jsoupDocument = Jsoup.connect(url)
					.userAgent("\"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0\"")
					.timeout(10 * 1000).get();
			
			Element element = jsoupDocument.select("[data-src]").get((int) (Math.random() * randomNumber));
			imageUrl = element.attr("abs:data-src");
		} catch (Exception e) {
			System.out.println(e);
		}

		return imageUrl;
	}

}