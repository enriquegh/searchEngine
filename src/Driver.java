import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class Driver {

	public static void main(String[] args) throws IOException {
		// TODO
		ArgumentParser ap = new ArgumentParser(args);
		
		if (ap.hasFlag("-i") && !ap.hasValue("-i")){
			Path indexPath = Paths.get(".","index.txt").toAbsolutePath();
			Files.createFile(indexPath);
		}
		
		if (ap.hasFlag("-i") && ap.hasValue("-i")){
			String outputPath = ap.getValue("-i");
		}
		
		if (ap.hasFlag("-d") && ap.hasValue("-d")){
			String directoryPath = ap.getValue("-d");
			Path path = Paths.get(directoryPath);
			RecursiveDirectoryStream.traverse(path);

		}

		
		WordParser wp = new WordParser();
	}
	
}
