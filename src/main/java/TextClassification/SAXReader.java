package TextClassification;

import java.io.Writer;

import org.xml.sax.helpers.DefaultHandler;

public class SAXReader extends DefaultHandler {

	static final String newLine = System.getProperty("line.separator");

	public static Writer out = null;

	private StringBuffer textBuffer = null;

	private String beginningOfLine = "";

}
