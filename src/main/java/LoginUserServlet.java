import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles login requests.
 *
 *
 */
@SuppressWarnings("serial")
public class LoginUserServlet extends LoginBaseServlet {

	STGroup templates = new STRawGroupDir("src/main/resources",'$', '$');


	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ST user = templates.getInstanceOf("user");
		user.add("title","Login");
		user.add("nullError",false);
        user.add("date",getDate());

		PrintWriter out = response.getWriter();
		String error = request.getParameter("error");
		int code = 0;

		if (error != null) {
			try {
				code = Integer.parseInt(error);
			}
			catch (Exception ex) {
				code = -1;
			}

			String errorMessage = getStatusMessage(code);
			user.add("nullError",true);
			user.add("errorMessage",errorMessage);
		}

		user.add("newUser", request.getParameter("newuser") != null);

		if (request.getParameter("logout") != null) {
			clearCookies(request, response);
            user.add("logout", true);
		}

        user.add("action","/login");
        user.add("actionValue","Login");
        user.add("registerLink",true);

		out.print(user.render());
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");

		Status status = dbhandler.authenticateUser(user, pass);

		try {
			if (status == Status.OK) {
				// should eventually change this to something more secure
				response.addCookie(new Cookie("login", "true"));
				response.addCookie(new Cookie("name", user));
				response.sendRedirect(response.encodeRedirectURL("/welcome"));
			}
			else {
				response.addCookie(new Cookie("login", "false"));
				response.addCookie(new Cookie("name", ""));
				response.sendRedirect(response.encodeRedirectURL("/login?error=" + status.ordinal()));
			}
		}
		catch (Exception ex) {
			log.error("Unable to process login form.", ex);
		}
	}
}
