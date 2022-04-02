package com.enriquegh.searchEngine;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Redirects to welcome page or login page depending on whether user
 * session is detected.
 *
 * @see LoginServer
 */
@SuppressWarnings("serial")
public class LoginRedirectServlet extends LoginBaseServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (getUsername(request) != null) {
			response.sendRedirect("/welcome");
		}
		else {
			response.sendRedirect("/search");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}
