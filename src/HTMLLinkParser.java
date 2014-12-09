import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * For this homework assignment, you must create a regular expression that is
 * able to parse links from HTML. Your code may assume the HTML is valid, and
 * all attributes are properly quoted and URL encoded.
 * 
 * <p>
 * See the following link for details on the HTML Anchor tag: <a
 * href="https://developer.mozilla.org/en-US/docs/Web/HTML/Element/a">
 * https://developer.mozilla.org/en-US/docs/Web/HTML/Element/a </a>
 * 
 * You must ALSO fill in 5 unit tests in {@link HTMLLinkParserExtraTest} that
 * you create yourself. This involves deciding on a test case and the expected
 * output. All other parts of each test have been provided.
 * 
 * @see HTMLLinkParser
 * @see HTMLLinkParserTest
 * @see HTMLLinkParserExtraTest
 */
public class HTMLLinkParser {
	private static Logger logger = LogManager.getLogger();

	/**
	 * The regular expression used to parse the HTML for links.
	 * 
	 * 
	 */
	public static final String REGEX = "(?is)<a[^>]*?href[^>]*?=[^>]*?\"([^\"]*?)\"(?:[^>]*?)?>";

	/**
	 * The group in the regular expression that captures the raw link. This will
	 * usually be 1, depending on your specific regex.
	 * 
	 * 
	 */
	public static final int GROUP = 1;

	/**
	 * Parses the provided text for HTML links. You should not need to modify
	 * this method.
	 * 
	 * @param html
	 *            - valid HTML code, with quoted attributes and URL encoded
	 *            links
	 * @return list of links found in HTML code
	 *
	 */
	public static ArrayList<String> listLinks(String html, String urlPath) {
		// list to store links
		ArrayList<String> links = new ArrayList<String>();
		URL base = null;
		try {
			base = new URL(urlPath);
		} catch (MalformedURLException e) {
			logger.debug(e);
		}

		// compile string into regular expression
		Pattern p = Pattern.compile(REGEX);

		// match provided textgainst regular expression

		if (html == null) {
			logger.debug("HTML is null! " + urlPath);
		}

		Matcher m = p.matcher(html);

		// loop through every match found in text
		while (m.find()) {
			// add the appropriate group from regular expression to list

			if (m.group(GROUP).startsWith("http")) {
				links.add(m.group(GROUP));
			} else {
				URL absolute;
				try {
					absolute = new URL(base, m.group(GROUP));
					String path = absolute.getProtocol() + "://"
							+ absolute.getHost() + absolute.getPath();
					links.add(path);
				} catch (MalformedURLException e) {
					logger.debug(e);
				}
			}
		}
		return links;
	}
}
