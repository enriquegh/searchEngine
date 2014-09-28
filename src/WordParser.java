import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * This class parses through text removing unnecessary spaces and special
 * characters and assumes words will be separated by whitespace.
 */
public class WordParser {

    /**
     * Converts text into a consistent format by converting text to lower-case,
     * replacing non-word characters and underscores with a single space, and
     * finally removing leading and trailing whitespace. (See the {@link String}
     * class for several helpful methods.)
     *
     * @param text
     *            - original text
     * @return text without special characters and leading or trailing spaces
     */
    public static String cleanText(String text) {

        text = text.toLowerCase();

        text = text.replaceAll("\n", "");
        text = text.replaceAll("[\\W_]+", " ");
        
        text = text.trim();

        return text;
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

        String[] wordsString = cleanText(text).split(" ");
        for (String word : wordsString) {
            if (!word.isEmpty()) {
                words.add(word);
            }
        }

        return words;
    }

    /**
     * Reads a file line-by-line and parses the resulting line into words using
     * the {@link #parseText(String)} method. Adds the parsed words to a master
     * list of words, which is returned at the end.
     *
     * @param path
     *            - file path to open
     * @return list of cleaned words
     * @throws IOException
     */
    public static List<String> parseFile(Path path) throws IOException {
        ArrayList<String> words = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path,
                Charset.forName("UTF-8"));)

        {
            String line = null;

            while ((line = reader.readLine()) != null) {
                words.addAll(parseText(line));
            }

        }

        return words;
    }
}
