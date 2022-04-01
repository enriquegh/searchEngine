package com.enriquegh.searchEngine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;

/**
 * This class is designed to test your database configuration. You need to have
 * a database.properties file with username, password, database, and hostname.
 * You must also have the tunnel to stargate.cs.usfca.edu running if you are
 * off-campus.
 */
public class DatabaseConnector {

	/**
	 * URI to use when connecting to database. Should be in the format:
	 * jdbc:subprotocol://hostname/database
	 */
	public String uri;

	/** Properties with username and password for connecting to database. */
	private Properties login;

	/**
	 * Creates a connector from JDBC_DATABASE_URL, JDBC_DATABASE_USERNAME and JDBC_DATABASE_PASSWORD variables
	 *
	 */
	public DatabaseConnector() throws FileNotFoundException, IOException {

        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");

        initializeConnector(dbUrl, username, password);

	}

	/**
	 * Creates a connector from the provided database properties file.
	 *
	 * @param configPath path to the database properties file
	 * @throws IOException if unable to properly parse properties file
	 * @throws FileNotFoundException if properties file not found
	 */
	public DatabaseConnector(String configPath)
			throws FileNotFoundException, IOException {

		// Try to load the configuration from file
		Properties config = loadConfig(configPath);

		String hostname = config.getProperty("hostname");
        String database = config.getProperty("database");
        String username = config.getProperty("username");
        String password = config.getProperty("password");

        initializeConnector(hostname, database, username, password);

	}


    private void initializeConnector(String hostname, String database, String username, String password) {
        // Create database URI in proper format
        String dbURI = String.format("jdbc:mysql://%s/%s",
                hostname,
                database);

        initializeConnector(dbURI, username, password);
    }

    private void initializeConnector(String databaseURI, String username, String password) {

        uri = databaseURI;

        // Create database login properties
        login = new Properties();
        login.put("user", username);
        login.put("password", password);
    }

	/**
	 * Attempts to load properties file with database configuration. Must
	 * include username, password, database, and hostname.
	 *
	 * @param configPath path to database properties file
	 * @return database properties
	 * @throws IOException if unable to properly parse properties file
	 * @throws FileNotFoundException if properties file not found
	 */
	private Properties loadConfig(String configPath)
			throws FileNotFoundException, IOException {

		// Specify which keys must be in properties file
		Set<String> required = new HashSet<>();
		required.add("username");
		required.add("password");
		required.add("database");
		required.add("hostname");

		// Load properties file
		Properties config = new Properties();
		config.load(new FileReader(configPath));

		// Check that required keys are present
		if (!config.keySet().containsAll(required)) {
			String error = "Must provide the following in properties file: ";
			throw new InvalidPropertiesFormatException(error + required);
		}

		return config;
	}

	/**
	 * Attempts to connect to database using loaded configuration.
	 *
	 * @return database connection
	 * @throws SQLException if unable to establish database connection
	 */
	public Connection getConnection() throws SQLException {
		Connection dbConnection = DriverManager.getConnection(uri, login);
		dbConnection.setAutoCommit(true);
		return dbConnection;
	}

	/**
	 * Opens a database connection and returns a set of found tables. Will
	 * return an empty set if there are no results.
	 *
	 * @return set of tables
	 */
	public Set<String> getTables(Connection db) throws SQLException {
		Set<String> tables = new HashSet<>();

		// Create statement and close when done.
		// Database connection will be closed elsewhere.
		try (Statement sql = db.createStatement();) {
			if (sql.execute("SHOW TABLES;")) {
				ResultSet results = sql.getResultSet();

				while (results.next()) {
					tables.add(results.getString(1));
				}
			}
		}

		return tables;
	}

	/**
	 * Opens a database connection, executes a simple statement, and closes
	 * the database connection.
	 *
	 * @return true if all operations successful
	 */
	public boolean testConnection() {
		boolean okay = false;

		// Open database connection and close when done
		try (Connection db = getConnection();) {
			System.out.println("Executing SHOW TABLES...");
			Set<String> tables = getTables(db);

			if (tables != null) {
				System.out.print("Found " + tables.size() + " tables: ");
				System.out.println(tables);

				okay = true;
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return okay;
	}

	/**
	 * Tests whether database configuration (including tunnel) is correct. If
	 * you see the message "Connection to database established" then your
	 * settings are correct
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		try {

			DatabaseConnector test = new DatabaseConnector("database.properties");
			System.out.println("Connecting to " + test.uri);

			if (test.testConnection()) {
				System.out.println("Connection to database established.");
			}
			else {
				System.err.println("Unable to connect properly to database.");
			}
		}
		catch (Exception e) {
			System.err.println("Unable to connect properly to database.");
			System.err.println(e.getMessage());
		}
	}
}
