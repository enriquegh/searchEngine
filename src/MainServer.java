import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class MainServer {

	private final int PORT;
	private static Logger logger = Log.getRootLogger();

	public MainServer(int port) {
		PORT = port;
	}

	public MainServer() {
		PORT = 8080;
	}

	public void run(ThreadSafeInvertedIndex index) {
		Server server = new Server(PORT);
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(new ServletHolder(new MessageServlet()),
				"/pie");
		handler.addServletWithMapping(new ServletHolder(new MessageServlet()),
				"/cake");
		handler.addServletWithMapping(new ServletHolder(new SearchServlet(index)),
				"/search");
		server.setHandler(handler);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug(e);
		}

	}

}
