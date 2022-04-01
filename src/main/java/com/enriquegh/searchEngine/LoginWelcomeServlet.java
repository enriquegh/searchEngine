package com.enriquegh.searchEngine;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles display of user information.
 *
 */
@SuppressWarnings("serial")
public class LoginWelcomeServlet extends LoginBaseServlet {

	STGroup templates = new STRawGroupDirPatched("templates",'$', '$');

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String user = getUsername(request);

		if (user != null) {

            ST welcome = templates.getInstanceOf("welcome");
			PrintWriter out = response.getWriter();

            welcome.add("title","Welcome");
            welcome.add("user",user);
            welcome.add("date",getDate());

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
