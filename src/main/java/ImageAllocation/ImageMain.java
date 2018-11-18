package ImageAllocation;

import java.io.File;
import java.io.IOException;

/**
 * @author Huber
 */
public class ImageMain {

	public static void main(String[] args) throws IOException {
		ImageAllocation imageAllocation = new ImageAllocation();
		System.out.println(imageAllocation.searchImage("baum"));
		imageAllocation.getImage(
				"https://de.wikipedia.org/wiki/Duale_Hochschule_Baden-W%C3%BCrttemberg_Mannheim#/media/File:DHBW_MA_Logo.jpg",
				new File("").getAbsolutePath());
	}
}
