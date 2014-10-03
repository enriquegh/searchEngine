import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

// TODO Javadoc, describes how to run the program

/**
 * This class runs all the methods and classes needed to make search
 * engine work. Driver calls {@link ArgumentParser} & {@link InvertedIndex}.
 * Checks that the flag -d and a directory are entered.
 * If flag -i is entered an output directory will be included.
 * If flag is included but no directory it will be saved to "index.txt"
 */
public class Driver {

    public static void main(String[] args) throws IOException {
        String outputPath;
        ArgumentParser ap = new ArgumentParser(args);
        InvertedIndex wi = new InvertedIndex();

        if (ap.hasFlag("-d") && ap.hasValue("-d")) {
            String directoryPath = ap.getValue("-d");
            try {
                Path path = Paths.get(directoryPath);
                InvertedIndexBuilder.traverse(path, wi);

            } catch (NoSuchFileException x) {
                System.err.format("%s: no such" + " file or directory%n",
                        directoryPath);

            } catch (IOException x) {
                System.err.format("%s%n", x);
            }

        }

        if (ap.hasFlag("-i") && !ap.hasValue("-i")) {
            String indexPathString = "index.txt";
            try {
                Path indexPath = Paths.get(".", "index.txt");
                Files.createFile(indexPath);

            } catch (NoSuchFileException x) {
                System.err.format("%s: no such" + " file or directory%n",
                        indexPathString);

            } catch (IOException x) {
                System.err.format("%s%n", x);
            }

        }

        if (ap.hasFlag("-i") && ap.hasValue("-i")) {
            outputPath = ap.getValue("-i");
            wi.print(outputPath);

        }

    }
}
