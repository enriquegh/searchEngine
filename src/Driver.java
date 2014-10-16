import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class runs all the methods and classes needed to make search
 * engine work. Driver calls {@link ArgumentParser} & {@link InvertedIndex}.
 * Checks that the flag -d and a directory are entered.
 * If flag -i is entered an output directory will be included.
 * If flag is included but no directory it will be saved to "index.txt"
 */
public class Driver {
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException {
        String outputPath;
        String searchPath;
        ArgumentParser parser = new ArgumentParser(args);
        InvertedIndex index = new InvertedIndex();

        
//        for(String arg : args){
//            logger.debug(arg);  
//        }

        if (parser.hasFlag("-d") && parser.hasValue("-d")) {
            String directoryPath = parser.getValue("-d");
            try {
                Path path = Paths.get(directoryPath);
                InvertedIndexBuilder.traverse(path, index);

            } catch (NoSuchFileException x) {
                System.err.format("%s: no such" + " file or directory%n",
                        directoryPath);

            } catch (IOException x) {
                System.err.format("%s%n", x);
            }

        }

        if (parser.hasFlag("-i") && !parser.hasValue("-i")) {
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

        if (parser.hasFlag("-i") && parser.hasValue("-i")) {
            outputPath = parser.getValue("-i");
            index.print(outputPath);

        }
        
        if (parser.hasFlag("-q") && parser.hasValue("-q")){
            String directoryPath = parser.getValue("-q");
            Path path = Paths.get(directoryPath);
            //InvertedIndexBuilder.traverse(path, index);
            QueryParser.parseFile(path, index);
            

        }
        
        if (parser.hasFlag("-s") && !parser.hasValue("-s")){
            //logger.debug("Trying to create search.txt file");
            String searchPathString = "search.txt";
            try {
                Path indexPath = Paths.get(".", "search.txt");
                Files.createFile(indexPath);
                //logger.debug("Trying to create search.txt file");
              

            } catch (NoSuchFileException x) {
                System.err.format("%s: no such" + " file or directory%n",
                        searchPathString);

            } catch (IOException x) {
                System.err.format("%s%n", x);
            }
        }
        
        if (parser.hasFlag("-s") && parser.hasValue("-s")){
            outputPath = parser.getValue("-s");
            //index.print(outputPath);
            //logger.debug(outputPath);
            
        }

    }
}
