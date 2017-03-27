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
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;
// More XSS Prevention:
// https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet
// Apache Comments:
// http://commons.apache.org/proper/commons-lang/download_lang.cgi

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet {
	private static final String TITLE = "Welcome to ChaChing";
	private static Logger log = Log.getRootLogger();
	private final ThreadSafeInvertedIndex index;
	private final ArrayList<SearchResult> results;
	private static boolean firstTime = true;
    STGroup templates = new STRawGroupDir("src/main/resources",'$', '$');


    public SearchServlet(ThreadSafeInvertedIndex index) {
		super();
		this.index = index;
		results = new ArrayList<>();
	}

	@Override
	protected void doGet(HttpServletRequest request,
						 HttpServletResponse response) throws ServletException, IOException {

        ST search = templates.getInstanceOf("search");
        PrintWriter out = response.getWriter();

        response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		log.info("MessageServlet ID " + this.hashCode()
				+ " handling GET request.");

		search.add("title",TITLE);
		search.add("requestPath",request.getServletPath());

		synchronized (results) {
		    search.add("results",results);
		    search.add("firstTime",firstTime);
		    search.add("isSizeZero",results.size() == 0);
		}

        out.print(search.render());
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
}