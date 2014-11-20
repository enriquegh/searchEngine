import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class parses through text removing unnecessary spaces and special
 * characters and assumes words will be separated by whitespace.
 */
public class QueryParser {
    private static Logger logger = LogManager.getLogger();
    protected final LinkedHashMap<String, ArrayList<SearchResult>> results; // TODO
                                                                            // Try
                                                                            // to
                                                                            // avoid,
                                                                            // and
                                                                            // access
                                                                            // via
                                                                            // public
                                                                            // methods
                                                                            // instead.

    /**
     * Instantiated the LinkedHashMap that will be used to save the results.
     */
    public QueryParser() {
        results = new LinkedHashMap<>();
    }

    /**
     * Parses a file by word and adds the whole line as key and adds
     * SearchResult ArrayList as value by calling {@link InvertedIndex#search}
     * 
     * @param file
     *            a {@link Path} instance of that contains the path to the input
     *            query.
     * @param index
     *            a {@link WordIndex} instance to access its methods.
     * @throws IOException
     */

    public void parseFile(Path file, InvertedIndex index) throws IOException {

        try (BufferedReader reader = Files.newBufferedReader(file,
                Charset.forName("UTF-8"));) {
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] wordsString = line.split(" ");
                results.put(line, index.search(wordsString));
            }
        }
    }

    /**
     * Prints out all the SearchResults found into a human readable file.
     * 
     * @param output
     *            {@link String} that contains the output file.
     * @throws IOException
     */
    public void print(String output) throws IOException {
        Path outputPath = Paths.get(output);
        logger.debug("Printing to {}", outputPath);

        if (!outputPath.toFile().isDirectory()) {

            try (BufferedWriter writer = Files.newBufferedWriter(outputPath,
                    Charset.forName("UTF-8"));) {

                for (Entry<String, ArrayList<SearchResult>> queryResult : results
                        .entrySet()) {
                    writer.write(queryResult.getKey());
                    writer.newLine();

                    for (SearchResult sr : queryResult.getValue()) {
                        writer.write(sr.toString());
                        writer.newLine();
                    }
                    writer.newLine();
                }
                writer.flush();
            }
        }
    }
}
