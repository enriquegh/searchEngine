import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles display of user information.
 *
 * @see LoginServer
 */
@SuppressWarnings("serial")
public class LoginWelcomeServlet extends LoginBaseServlet {

	STGroup templates = new STRawGroupDir("src/main/resources",'$', '$');

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String user = getUsername(request);

		if (user != null) {
//			prepareResponse("Welcome", response);
            ST welcome = templates.getInstanceOf("welcome");
			PrintWriter out = response.getWriter();

            welcome.add("title","Welcome");
            welcome.add("user",user);
            welcome.add("date",getDate());
//			out.println("<p>Hello " + user + "!</p>");
//			out.println("<p><a href=\"/search\">Go Search</a></p>");
//			out.println("<p><a href=\"/login?logout\">(logout)</a></p>");
//
//			finishResponse(response);

            out.print(welcome.render());
		}
		else {
			response.sendRedirect("/login");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}
