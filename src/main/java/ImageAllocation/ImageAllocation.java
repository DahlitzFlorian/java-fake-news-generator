package ImageAllocation;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

	public String searchImage(String keyword) throws IOException {
		URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?"
				+ "v=1.0&q=barack%20obama&userip=INSERT-USER-IP");
		String finalWebsite;
		URLConnection internetCon = url.openConnection();
		internetCon.addRequestProperty(keyword, "http//google.com/"); // If we want to use google

		StringBuilder stringBuilder = new StringBuilder();
		String lineAtWebsite;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(internetCon.getInputStream()));
		while ((lineAtWebsite = bufferedReader.readLine()) != null) {
			stringBuilder.append(lineAtWebsite);
		}

		return finalWebsite = stringBuilder.toString();

	}

}