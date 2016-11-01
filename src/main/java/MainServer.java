import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * 
 * This class is the Main Server that controls all the servlets that are being used.
 *
 */
public class MainServer {

	private final int PORT;
	private static Logger logger = Log.getRootLogger();

	/**
	 * Initializes port to whatever parameter is given
	 * @param port
	 */
	public MainServer(int port) {
		PORT = port;
	}
	/**
	 * Port is initialized to default 8080
	 */
	public MainServer() {
		PORT = 8080;
	}

	public void run(ThreadSafeInvertedIndex index) {
		Server server = new Server(PORT);
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(new ServletHolder(new SearchServlet(index)), "/search");
		handler.addServletWithMapping(LoginUserServlet.class,     "/login");
		handler.addServletWithMapping(LoginRegisterServlet.class, "/register");
		handler.addServletWithMapping(LoginWelcomeServlet.class,  "/welcome");
		handler.addServletWithMapping(LoginRedirectServlet.class, "/*");
		server.setHandler(handler);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			logger.debug(e);
		}

	}

}
