import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

 /**
  * This class parses through text removing unnecessary spaces and special
  * characters and assumes words will be separated by whitespace.
  */
public class QueryParser {
    
    
    private static Logger logger = LogManager.getLogger();


    public static void parseFile(Path file, InvertedIndex index) throws IOException {
        
        try (BufferedReader reader = Files.newBufferedReader(file,
                Charset.forName("UTF-8"));)

        {
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                String[] wordsString = line.split(" ");
                index.search(wordsString);

            }
        }
        
    }
    /**
     * Splits text into words by whitespaces, cleans the resulting words using
     * {@link #cleanText(String)} so that they are in a consistent format, and
     * adds non-empty words to an {@link ArrayList}.
     *
     *
     * @param text
     *            - original text
     * @return list of cleaned words
     */
    public static List<String> parseText(String text) {
        ArrayList<String> words = new ArrayList<>();

        String[] wordsString = text.split(" ");
        
        for (String word : wordsString) {
            if (!word.isEmpty()) {
                words.add(word);
            }
        }

        return words;
    }

    
}
