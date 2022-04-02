package com.enriquegh.searchEngine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides base functionality to all servlets in this example.
 *
 *
 */
@SuppressWarnings("serial")
public class LoginBaseServlet extends HttpServlet {

	protected static Logger log = LogManager.getLogger();
	protected static final LoginDatabaseHandler dbhandler = LoginDatabaseHandler.getInstance();

	protected String getDate() {
		String format = "hh:mm a 'on' EEE, MMM dd, yyyy";
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	protected Map<String, String> getCookieMap(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				map.put(cookie.getName(), cookie.getValue());
			}
		}

		return map;
	}

	protected void clearCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();

		if(cookies == null) {
			return;
		}

		for(Cookie cookie : cookies) {
			clearCookie(cookie,response);
		}
	}

	protected void clearCookie(Cookie cookie, HttpServletResponse response) {
		cookie.setValue("");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	protected void debugCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if(cookies == null) {
			log.info("Saved Cookies: []");
		}
		else {
			String[] names = new String[cookies.length];

			for(int i = 0; i < names.length; i++) {
				names[i] = String.format("(%s, %s, %d)",
						cookies[i].getName(),
						cookies[i].getValue(),
						cookies[i].getMaxAge());
			}

			log.info("Saved Cookies: " + Arrays.toString(names));
		}
	}

	protected String getStatusMessage(String errorName) {
		Status status = null;

		try {
			status = Status.valueOf(errorName);
		}
		catch (Exception ex) {
			log.debug(errorName, ex);
			status = Status.ERROR;
		}

		return status.toString();
	}

	protected String getStatusMessage(int code) {
		Status status = null;

		try {
			status = Status.values()[code];
		}
		catch (Exception ex) {
			log.debug(ex.getMessage(), ex);
			status = Status.ERROR;
		}

		return status.toString();
	}

	protected String getUsername(HttpServletRequest request) {
		Map<String, String> cookies = getCookieMap(request);

		String login = cookies.get("login");
		String user  = cookies.get("name");

		if ((login != null) && login.equals("true") && (user != null)) {
			// this is not safe!
			return user;
		}

		return null;
	}
}
