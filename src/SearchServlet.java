import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
// More XSS Prevention:
// https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet
// Apache Comments:
// http://commons.apache.org/proper/commons-lang/download_lang.cgi

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet {
	private static final String TITLE = "Welcome to ChaChing";
	private static Logger log = Log.getRootLogger();
	private LinkedList<String> messages;
	private final ThreadSafeInvertedIndex index;
	ArrayList<SearchResult> results;
	private static boolean firstTime = true;

	public SearchServlet(ThreadSafeInvertedIndex index) {
		super();
		this.index = index;
		messages = new LinkedList<>();
		results = new ArrayList<>();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		log.info("MessageServlet ID " + this.hashCode()
				+ " handling GET request.");
		PrintWriter out = response.getWriter();
		out.printf("<html>%n");
		out.printf("<head><title>%s</title></head>%n", TITLE);
		out.printf("<body background=\"http://cs.usfca.edu/usf-in-pictures/IMG_3515.jpg\" link=\"white\" vlink=\"#A5AAB6\">%n");
		out.printf("<center>");
		out.printf("<h1>ChaChing</h1>%n%n");
		printSearchBar(request, response);

		synchronized (results) {
			for (SearchResult result : results) {
				out.printf("<p><a href= \"%s\">%s</a></p>%n%n", result.getPath(), result.getPath());
			}
			if (results.size() == 0 && !firstTime) {
				out.printf("<p>Yikes! Looks like there are no results for your query.</p>%n%n");
			}
		}
		out.printf("</center>");
		out.printf("</body>%n");
		out.printf("</html>%n");
		firstTime = true;
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		log.info("MessageServlet ID " + this.hashCode()
				+ " handling POST request.");
		
		results.clear(); //Each search is new
		String searchQuery = request.getParameter("searchBar");
		String[] queryList = searchQuery.split(" ");
		firstTime = false;
		synchronized(results) {
			if (!searchQuery.equals("")) {
				results.addAll(index.search(queryList));
			}
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(request.getServletPath());
	}

	
	private static void printSearchBar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		out.printf("<form method=\"post\" action=\"%s\">%n",
				request.getServletPath());
		out.printf("<input type=\"text\" name=\"searchBar\" maxlength=\"50\" size=\"20\">%n");
		out.printf("<p><input type=\"submit\" value=\"Search\"></p>\n%n");
		out.printf("</form>\n%n");
		
	}

	private static String getDate() {
		String format = "hh:mm a 'on' EEEE, MMMM dd yyyy";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date());
	}
}