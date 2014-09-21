import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class Driver {

	public static void main(String[] args) throws IOException {
		// TODO
		String outputPath;
		ArgumentParser ap = new ArgumentParser(args);
		WordIndex wi = new WordIndex();
		
			if (ap.hasFlag("-d") && ap.hasValue("-d")){
				String directoryPath = ap.getValue("-d");
				try {
					Path path = Paths.get(directoryPath);
					RecursiveDirectoryStream.traverse(path,wi);
					
				}
				catch (NoSuchFileException x) {
				    System.err.format("%s: no such" + " file or directory%n", directoryPath);

				} catch (IOException x) {
				    System.err.format("%s%n", x);
				
				
				//changed traverse from private static void to static void

	
			}
	
			if (ap.hasFlag("-i") && !ap.hasValue("-i")){
				Path indexPath = Paths.get(".","index.txt");
				Files.createFile(indexPath);
			}
			
			
			if (ap.hasFlag("-i") && ap.hasValue("-i")){
				outputPath = ap.getValue("-i");
				wi.print(outputPath);
	
			}
			

		}
	}
	
}
