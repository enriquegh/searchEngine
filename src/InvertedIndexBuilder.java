import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class traverses over all the files on a given folder figures out
 * if files are text parses them with {@link WordParser} and adds them into {@link InvertedIndex }.
 *
 * <p>
 * <em>
 * Note that this class is designed to illustrate a specific concept, and
 * may not be an example of proper class design outside of this context.
 * </em>
 * </p>
 *
 */
public class InvertedIndexBuilder {

    /**
     * Outputs the name of the file or subdirectory, with proper indentation to
     * help indicate the hierarchy. If a subdirectory is encountered, will
     * recursively list all the files in that subdirectory.
     *
     *
     * @param path
     *            to retrieve the listing, assumes a directory and not a file is
     *            passed
     * @param index
     *            WordIndex instance where the the files, words and position will be stored
     * @return
     * @throws IOException
     */

    public static void traverse(Path path, InvertedIndex index) throws IOException {

        try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {

            for (Path file : listing) {
                
                String fileName = file.getFileName().toString().toLowerCase();

                if (fileName.endsWith(".txt")) {
                    parseFile(file, index);
                }

                // If it is a subdirectory, recursively traverse.
                if (Files.isDirectory(file)) {
                    traverse(file, index);
                }
            }
        }
    }

    // TODO Missing Javadoc
    public static void parseFile(Path file, InvertedIndex index) throws IOException {
        
        try (BufferedReader reader = Files.newBufferedReader(file,
                Charset.forName("UTF-8"));)

        {
            String line = null;
            int i = 1;
            
            while ((line = reader.readLine()) != null) {
  
                String[] wordsString = WordParser.cleanText(line).split(" ");
                
                for (String word : wordsString) {

                    if (!word.isEmpty()) {
                        index.add(word, file.toString(), i);
                        i++;
                    }

                }
            }
        }
        
    }

}
