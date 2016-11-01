import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	IndexTest.IndexArgumentTest.class,
	IndexTest.IndexOutputTest.class
})
public class IndexTest {

    /** Configure this on your system if you want to have a longer timeout. */
    public static final int TIMEOUT = 60000;

	public static class IndexArgumentTest {

		@Test(timeout = TIMEOUT)
		public void testNoDirectory() {
			Path output = Paths.get(ProjectTest.RESULT_DIR, "index-nodir.txt");
			String[] args = {ProjectTest.DIR_FLAG, output.toString()};

			ProjectTest.checkExceptions("No Directory", args);
		}

		@Test(timeout = TIMEOUT)
		public void testBadDirectory() {
			String name = Long.toHexString(Double.doubleToLongBits(Math.random()));

			Path input  = Paths.get(name);
			Path output = Paths.get(ProjectTest.RESULT_DIR, "index-baddir.txt");

			String[] args = {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.INDEX_FLAG, output.toString()
			};

			ProjectTest.checkExceptions("Bad Directory", args);
		}

		@Test(timeout = TIMEOUT)
		public void testDefaultOutput() throws IOException {
			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path output = Paths.get(ProjectTest.INDEX_DEFAULT);

			String[] args = {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.INDEX_FLAG
			};

			Files.deleteIfExists(output);
			ProjectTest.checkExceptions("Default Output", args);

			String errorMessage = String.format(
					"%n" + "Test Case: %s%n" + "%s%n",
					"Default Output",
					"Output to index.txt if output path not provided");

			Assert.assertTrue(errorMessage, Files.isReadable(output));
		}

		@Test(timeout = TIMEOUT)
		public void testNoOutput() throws IOException {
            Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
            Path output = Paths.get(ProjectTest.INDEX_DEFAULT);

			String[] args = {
					ProjectTest.DIR_FLAG, input.toString()
			};

			Files.deleteIfExists(output);
			ProjectTest.checkExceptions("No Output", args);

			String errorMessage = String.format(
					"%n" + "Test Case: %s%n" + "%s%n",
					"No Output",
					"Do not create index.txt unless proper flag provided");

			Assert.assertFalse(errorMessage, Files.isReadable(output));
		}
	}

	public static class IndexOutputTest {

		@Test(timeout = TIMEOUT)
		public void testIndexSimple() {

			String name = "index-simple.txt";

            Path input = Paths.get(ProjectTest.INPUT_DIR, "simple");
            Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
			Path result = Paths.get(ProjectTest.RESULT_DIR, name);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.INDEX_FLAG, result.toString()
			};

			ProjectTest.checkProjectOutput("Index Simple", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testIndexSimpleReversed() {
			String name = "index-simple-reversed.txt";

            Path input = Paths.get(ProjectTest.INPUT_DIR, "simple");
            Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
            Path result = Paths.get(ProjectTest.RESULT_DIR, name);

            String[] args = new String[] {
                    ProjectTest.INDEX_FLAG, result.toString(),
                    ProjectTest.DIR_FLAG, input.toString()
            };

			ProjectTest.checkProjectOutput("Index Simple Reversed", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testIndexRFCs() {
			String name = "index-rfcs.txt";

            Path input = Paths.get(ProjectTest.INPUT_DIR, "rfcs");
            Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
            Path result = Paths.get(ProjectTest.RESULT_DIR, name);

            String[] args = new String[] {
                    ProjectTest.DIR_FLAG, input.toString(),
                    ProjectTest.INDEX_FLAG, result.toString()
            };

			ProjectTest.checkProjectOutput("Index RFCs", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testIndexGutenberg() {
			String name = "index-guten.txt";

            Path input = Paths.get(ProjectTest.INPUT_DIR, "gutenberg");
            Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
            Path result = Paths.get(ProjectTest.RESULT_DIR, name);

            String[] args = new String[] {
                    ProjectTest.DIR_FLAG, input.toString(),
                    ProjectTest.INDEX_FLAG, result.toString()
            };

			ProjectTest.checkProjectOutput("Index Gutenberg", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testIndexAll() {
			String name = "index-all.txt";

            Path input = Paths.get(ProjectTest.INPUT_DIR);
            Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
            Path result = Paths.get(ProjectTest.RESULT_DIR, name);

            String[] args = new String[] {
                    ProjectTest.DIR_FLAG, input.toString(),
                    ProjectTest.INDEX_FLAG, result.toString()
            };

			ProjectTest.checkProjectOutput("Index All", args, result, output);
		}
	}
}
