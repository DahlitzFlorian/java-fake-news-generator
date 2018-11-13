package Textbeschaffung;

import java.net.URL;
import java.util.*;

import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.XmlReader;
//import org.json.JSONObject;
import org.json.*;
import java.net.URL;
import java.util.regex.Pattern;

/*
Class for text creation. The input parameter is a keyword. The artikelBeschaffung method searches in the given rss feeds for news which are containing the keyword.


*/
public class Textbeschaffung implements NachrichtenBeschaffung {

	public JSONObject artikelBeschaffung(String keyword) {
		JSONObject artikelJSON = new JSONObject();
		ArrayList<String> urls = new ArrayList<String>();
		urls.add(
				"https://www.freefullrss.com/feed.php?url=http%3A%2F%2Fwww.spiegel.de%2Fwirtschaft%2Findex.rss&max=10&links=preserve&exc=&submit=Create+Full+Text+RSS");
		urls.add(
				"https://www.freefullrss.com/feed.php?url=http%3A%2F%2Fwww.tagesschau.de%2Fxml%2Frss2&max=5&links=preserve&exc=&submit=Create+Full+Text+RSS");
		urls.add(
				"https://www.freefullrss.com/feed.php?url=http%3A%2F%2Frss.sueddeutsche.de%2Frss%2FTopthemen&max=5&links=preserve&exc=&submit=Create+Full+Text+RSS");

		String nachricht;
		String headline;
		for (Iterator u = urls.iterator(); u.hasNext();) {
			try {

				URL feedUrl = new URL(u.next().toString());
				SyndFeedInput feedinput = new SyndFeedInput(); // create a Feed input
				SyndFeed nachrichtenFeed = feedinput.build(new XmlReader(feedUrl)); // Build reader for the RSSFeed

				for (Iterator i = nachrichtenFeed.getEntries().iterator(); i.hasNext();) {
					SyndEntry ursprungsArtikel = (SyndEntry) i.next(); // Every Feed entry is one Article
					nachricht = ursprungsArtikel.getDescription().toString(); // Receiving the reference article
					headline = ursprungsArtikel.getTitle().toString(); // Receiving the headling

					if (nachricht.contains(keyword) == true) {
						Textbeschaffung JSONbuilder = new Textbeschaffung();
						artikelJSON = JSONbuilder.artikelJSONBuilder(artikelJSON, nachricht, headline); // Call method
																										// to build
																										// return JSON
						System.out.print("Array ist" + artikelJSON);
					}

				}

			} catch (Exception e) {
				System.out.println("Error" + e.getMessage());
			}

		}
		return artikelJSON;
	}

	public String artikelCutter(String artikel) { // class to cut the meta information from the reference articles
		artikel = artikel.replaceAll("SyndContentImpl.*", "");
		artikel = artikel.replaceAll("\\<.*?>", "");
		return artikel;
	}

	public JSONObject artikelJSONBuilder(JSONObject artikelJson, String News, String titel) { // Input are a JSON
																								// article, the news,
																								// and the headline
		Textbeschaffung cutter = new Textbeschaffung();
		News = cutter.artikelCutter(News); // first cutting out all meta information
		artikelJson.put("ressource", News); // each reference article is a ressource
		artikelJson.put("title", titel);
		JSONObject paragraphObject = new JSONObject();
		JSONArray paragraphArray = new JSONArray();
		JSONArray tags = new JSONArray(); // empty JSON array for tags
		paragraphObject.put("tags", tags); // tags are added in the Textanalyse
		paragraphObject.put("content", "");
		paragraphArray.put(paragraphObject);
		artikelJson.put("paragraph", paragraphArray);
		return artikelJson;
	}
}
// Created and edited by Fichte