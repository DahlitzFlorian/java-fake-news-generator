package ImageAllocation;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Huber
 */
public class ImageAllocation {

	public void getImage(String url, String directoryFinal)
			throws IOException, FileNotFoundException, MalformedURLException {
		int counter = 0;
		URL urlImage = new URL(url);
		try (InputStream imageReaderInput = new BufferedInputStream(urlImage.openStream());
				OutputStream imageWriterOutput = new BufferedOutputStream(
						new FileOutputStream(directoryFinal + File.separator + FilenameUtils.getName(url)));) {

			while ((counter = imageReaderInput.read()) != -1) {
				imageWriterOutput.write(counter);
			}
		}
	}

	public String searchImage(String keyword) {

		String imageUrl = "";
		try {
			String url = "https://www.google.com/search?tbm=isch&q=" + keyword;
			Document jsoupDocument = Jsoup.connect(url)
					.userAgent("\"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0\"")
					.timeout(10 * 1000).get();

			Element element = jsoupDocument.select("[data-src]").first();
			String endOfURL = element.attr("abs:data-src");
			imageUrl = "<a href=\"http://images.google.com/search?tbm=isch&q=" + keyword + "\"><img src=\"" + endOfURL;
			System.out.println(imageUrl);

		} catch (Exception e) {
			System.out.println(e);
		}

		return imageUrl;
	}

	public int randomNumber() {
		int zahl = 0;
		return zahl = (int) (Math.random() * 10);
	}

}