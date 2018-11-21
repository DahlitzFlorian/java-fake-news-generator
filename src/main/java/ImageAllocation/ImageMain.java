package ImageAllocation;

import java.io.File;
import java.io.IOException;

/**
 * @author Huber
 */
public class ImageMain {
	public static void main(String[] args) throws IOException {

		ImageAllocation image = new ImageAllocation();
		String url = image.searchImage("Flugzeug");
		image.getImage(url, new File("").getAbsolutePath());

	}

}
