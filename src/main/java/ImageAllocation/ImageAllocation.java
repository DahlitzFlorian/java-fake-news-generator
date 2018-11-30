package ImageAllocation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Huber
 */
public class ImageAllocation extends ArrayList<Element> {

	private static final long serialVersionUID = 1L;

	public void getImage(String url, String directoryFinal) throws IOException {
		int counter = 0;
		URL urlImage = new URL(url);

		try (InputStream imageReaderInput = new BufferedInputStream(urlImage.openStream());
             OutputStream imageWriterOutput = new BufferedOutputStream(
                     new FileOutputStream(directoryFinal + File.separator + "image.jpg"))) {

			while ((counter = imageReaderInput.read()) != -1) {
				imageWriterOutput.write(counter);
			}
		}
	}

	public String searchImage(String keyword) {
		int randomNumber = 10;
		String imageUrl = "";
		try {
			String url = "https://www.google.com/search?tbm=isch&q=" + keyword;
			Document jsoupDocument = Jsoup.connect(url)
					.userAgent("\"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0\"")
					.timeout(10 * 1000).get();

			Element element = jsoupDocument.select("[data-src]").get((int) (Math.random() * randomNumber));
			imageUrl = element.attr("abs:data-src");

			System.out.println(imageUrl);
		} catch (Exception e) {
			System.out.println(e);
		}

		return imageUrl;
	}

	public Element randomNumber() {
		int randomNumber = 10;
		int zahl = 0;
		zahl = (int) (Math.random() * randomNumber);
		return isEmpty() ? null : get(zahl);
	}

}