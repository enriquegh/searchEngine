import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class LoginRegisterServlet extends LoginBaseServlet {

	STGroup templates = new STRawGroupDir("src/main/resources",'$', '$');

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

        ST register = templates.getInstanceOf("register");
        PrintWriter out = response.getWriter();

        register.add("title","Register New User");
        register.add("nullError",false);
        register.add("date",getDate());

		String error = request.getParameter("error");

		if(error != null) {
			String errorMessage = getStatusMessage(error);
			register.add("nullError",true);
			register.add("errorMessage",errorMessage);
		}

		register.add("action","/register");
		register.add("actionValue","Register");
        out.print(register.render());
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String newuser = request.getParameter("user");
		String newpass = request.getParameter("pass");
		Status status = dbhandler.registerUser(newuser, newpass);

		if(status == Status.OK) {
			response.sendRedirect(response.encodeRedirectURL("/login?newuser=true"));
		}
		else {
			String url = "/register?error=" + status.name();
			url = response.encodeRedirectURL(url);
			response.sendRedirect(url);
		}
	}
}
