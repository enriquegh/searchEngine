import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Taken idea from EliteConverter.java
public class FileReader {
	
	public static void readFile(String input) throws IOException{
		
		Path inputPath = Paths.get(input);

		try (
			BufferedReader reader =
					Files.newBufferedReader(inputPath, Charset.defaultCharset());
		) {
			String line = null;

			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
			}
		}
	}
	
	
}
