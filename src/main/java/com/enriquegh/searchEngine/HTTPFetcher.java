package com.enriquegh.searchEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An example class designed to make fetching the results of different HTTP
 * operations easier.
 */
public class HTTPFetcher {
	/** Port used by socket. For web servers, should be port 80. */
	public static final int PORT = 80;

	/** Version of HTTP used and supported. */
	public static final String version = "HTTP/1.1";

	/** Valid HTTP method types. */
	public static enum HTTP {
		OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
	};

	/** HTTP client Singleton that will be reused */
	private static OkHttpClient client = new OkHttpClient();

	private static Logger logger = LogManager.getLogger();

	/**
	 * Will fetch data from Response provided.
	 * It would be more efficient to operate on each line as returned
	 * instead of storing the entire result as a list.
	 *
	 * @param url
	 *            - url to fetch
	 * @param response
	 *            - full HTTP response
	 *
	 * @return the lines read from the web server
	 *
	 * @throws IOException
	 *
	 */
	public static Object fetchLines(URL url, Response response) {
		ArrayList<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(response.body().charStream());) {

			String line = null;

			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			logger.debug(e);
		}

		return lines;
	}


	/**
	 * Crafts an HTTP request for the provided method.
	 *
	 * @param url
	 *            - url to fetch
	 * @param type
	 *            - HTTP method to use
	 *
	 * @param body
	 * 			  - body to be sent
	 *
	 * @return Request instance
	 *
	 * @see {@link HTTP}
	 * @see {@link Request}
	 */
	public static Request craftHTTPRequest(URL url, HTTP type, RequestBody body) {

		return new Request.Builder().url(url).method(type.name(),body).build();

	}

	/**
	 * Crafts an HTTP request for the provided method.
	 *
	 * @param url
	 *            - url to fetch
	 * @param type
	 *            - HTTP method to use
	 *
	 * @return Request instance
	 *
	 * @see {@link HTTP}
	 * @see {@link Request}
	 */
	public static Request craftHTTPRequest(URL url, HTTP type) {

		return craftHTTPRequest(url, type, (RequestBody) null);

	}

	/**
	 * Fetches the headers for the specified URL.
	 *
	 * @param url
	 *            - url to fetch
	 * @return headers as a single {@link String}
	 *
	 */
	public static String fetchHeaders(String url) {
		URL target;
		Headers headers = null;
		Response response = null;
		try {
			target = new URL(url);
			Request request = craftHTTPRequest(target, HTTP.HEAD);
			response = executeHTTP(request);
			headers = response.headers();
			response.close();

		} catch (MalformedURLException e) {
			logger.debug(e);
		}

		return String.join(System.lineSeparator(), headers.toString());
	}

	/**
	 * Fetches the headers and HTML for the specified URL.
	 *
	 * @param url
	 *            - url to fetch
	 * @return headers and HTML as a single {@link String}
	 *
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String fetchAll(String url) throws MalformedURLException, IOException {
		URL target = new URL(url);
		Request request = craftHTTPRequest(target, HTTP.GET);
		Response response = executeHTTP(request);
		Headers headers = response.headers();

		String headersString = String.join(System.lineSeparator(), headers.toString());

		List<String> lines = (List<String>) fetchLines(target, response);

		response.close();

		String result = headersString + String.join(System.lineSeparator(), lines);
		return result;
	}

	/**
	 * Fetches the HTML for the specified URL (without headers).
	 *
	 * @param url
	 *            - url to fetch
	 * @return HTML as a single {@link String}, or null if not HTML
	 *
	 */
	public static String fetchHTML(String url) {
		URL target;
		Response response = null;
		try {
			target = new URL(url);
			Request request = craftHTTPRequest(target, HTTP.GET);
			response = executeHTTP(request);


			// Double-check this is an HTML file.
			String type = response.header("Content-Type");
			if (type != null && type.toLowerCase().contains("html")) {

				List<String> lines = (List<String>) fetchLines(target, response);
				return String.join(System.lineSeparator(), lines);
			}


        } catch (MalformedURLException e) {
            logger.debug(e);
        } catch (Exception e) {
            logger.debug(e);
        } finally {
			response.close();
		}

		return null;
	}

	/**
	 * Makes a new HTTP call from Request received
	 *
	 * @param request
	 *            - Request to be completed
	 * @return {@link Response}
	 *
	 */
	private static Response executeHTTP(Request request) {

		try {

			Response response = client.newCall(request).execute();
			return response;

		} catch (IOException e) {
		    logger.debug("Could not complete request to " + request.url());
			logger.debug(e.getMessage());

		}
		return null;
	}


	public static void main(String[] args) throws Exception {
		String url = "https://www.cs.usfca.edu/~sjengle/archived.html";
		System.out.println("***** HEADERS *****");
		System.out.println(fetchHeaders(url));
		System.out.println();

		System.out.println("***** HEADERS and HTML *****");
		System.out.println(fetchAll(url));
		System.out.println();

		System.out.println("***** HTML *****");
		System.out.println(fetchHTML(url));
		System.out.println();

		String image = "https://www.cs.usfca.edu/~sjengle/images/olivetrees.jpg";

		System.out.println("**** IMAGE HEADERS *****");
		System.out.println(fetchHeaders(image));
		System.out.println();

		System.out.println("**** IMAGE HTML *****");
		System.out.println(fetchHTML(image));
	}
}
