import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This class traverses over all the files on a given folder figures out
 * if files are text parses them with {@link WordParser} and adds them into {@link WordIndex }.
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

    public static void traverse(Path path, WordIndex index) throws IOException {

        try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {

            for (Path file : listing) {
                
                String fileName = file.getFileName().toString().toLowerCase();

                if (fileName.endsWith(".txt")) {
                    // TODO Functional but inefficient!
//                    List<String> words = WordParser.parseFile(file);
                    // TODO Call parseFile instead
                    index.addAll(WordParser.parseFile(file),file.toString(),1);

//                    for (int i = 0; i < words.size(); i++) {
//                        // Adding i+1 to get positions starting from 1
//                        index.add(words.get(i), file.toString(), i+1);
//                    }
                }

                // If it is a subdirectory, recursively traverse.
                if (Files.isDirectory(file)) {
                    traverse(file, index);
                }
            }
        }
    }

    // TODO Add for efficiency
    public static void parseFile(Path file, WordIndex index) {
        // Loop through the file line by line 
        // For every line, call WordParser.cleanText(), split here
        // Loop through the split words and add directly to the index
        
    }

}
