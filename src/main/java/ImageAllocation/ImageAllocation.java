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
}