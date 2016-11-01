import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
	SearchTest.SearchArgumentTest.class,
	SearchTest.SearchOutputTest.class
})
public class SearchTest {

    /** Configure this on your system if you want to have a longer timeout. */
    public static final int TIMEOUT = 60000;

	public static class SearchArgumentTest {

		@Test(timeout = TIMEOUT)
		public void testMissingQueryPath() {
            Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");

            String[] args = {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG
			};

			ProjectTest.checkExceptions("Missing Query Path", args);
		}

		@Test(timeout = TIMEOUT)
		public void testBadQueryPath() {
			String name = Long.toHexString(Double.doubleToLongBits(Math.random()));

			Path input = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query = Paths.get(name);

			String[] args = {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString()
			};

			ProjectTest.checkExceptions("Bad Query Path", args);
		}

		@Test(timeout = TIMEOUT)
		public void testDefaultOutput() throws IOException {
			Path input = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query = Paths.get(ProjectTest.QUERY_DIR, "simple.txt");
			Path result = Paths.get(ProjectTest.SEARCH_DEFAULT);

			String[] args = {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG
			};

			Files.deleteIfExists(result);
			ProjectTest.checkExceptions("Default Output", args);

			String errorMessage = String.format(
					"%n" + "Test Case: %s%n" + "%s%n",
					"Default Output",
					"Output to results.txt if output path not provided");

			Assert.assertTrue(errorMessage, Files.isReadable(result));
		}

		/**
		 * Tests that no output file is generated when the output flag is not
		 * present. Might also fail if {@link Files#deleteIfExists(Path)}
		 * throws an exception.
		 *
		 * @throws IOException if unable to delete results.txt file if it exists
		 */
		@Test(timeout = TIMEOUT)
		public void testNoOutput() throws IOException {
			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "simple.txt");
			Path result = Paths.get(ProjectTest.SEARCH_DEFAULT);

			String[] args = {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString()
			};

			Files.deleteIfExists(result);
			ProjectTest.checkExceptions("No Output", args);

			String errorMessage = String.format(
					"%n" + "Test Case: %s%n" + "%s%n",
					"No Output",
					"Do not create results.txt unless proper flag provided");

			Assert.assertFalse(errorMessage, Files.isReadable(result));
		}

		/**
		 * Tests whether program fails gracefully when attempt to write to
		 * a directory instead of a file path.
		 */
		@Test(timeout = TIMEOUT)
		public void testNoWriteDirectory() {
			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "simple.txt");

			try {
				Path path = Files.createTempDirectory(Paths.get(".").normalize(), "temp");
				path.toFile().deleteOnExit();

				String[] args = new String[] {
						ProjectTest.DIR_FLAG, input.toString(),
						ProjectTest.QUERY_FLAG, query.toString(),
						ProjectTest.SEARCH_FLAG, path.toString()};

				Driver.main(args);
			}
			catch (Exception e) {
				StringWriter writer = new StringWriter();
				e.printStackTrace(new PrintWriter(writer));

				Assert.fail(String.format(
						"%n" + "Test Case: %s%n" + "Exception: %s%n",
						"No Write Directory", writer.toString()));
			}
		}
	}

	public static class SearchOutputTest {

		@Test(timeout = TIMEOUT)
		public void testSearchSimple() {
			String name = "search-simple-simple.txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "simple.txt");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
			Path result = Paths.get(ProjectTest.RESULT_DIR, name);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG, result.toString()
			};

			ProjectTest.checkProjectOutput("Search Simple/Simple", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testSearchSimpleReversed() {
			String name = "search-simple-reversed.txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "simple.txt");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
			Path result = Paths.get(ProjectTest.RESULT_DIR, name);

			String[] args = new String[] {
					ProjectTest.SEARCH_FLAG, result.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.DIR_FLAG, input.toString()
			};

			ProjectTest.checkProjectOutput("Search Simple/Simple Reversed", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testSearchSimpleAlphabet() {
			String name = "search-simple-alpha.txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "alphabet.txt");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
			Path result = Paths.get(ProjectTest.RESULT_DIR, name);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG, result.toString()
			};

			ProjectTest.checkProjectOutput("Search Simple/Alpha", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testSearchSimpleComplex() {
			String name = "search-simple-complex.txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "complex.txt");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
			Path result = Paths.get(ProjectTest.RESULT_DIR, name);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG, result.toString()
			};

			ProjectTest.checkProjectOutput("Search Simple/Complex", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testSearchIndexSimple() {
			String name = "search-index-simple.txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR);
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "simple.txt");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
			Path result = Paths.get(ProjectTest.RESULT_DIR, name);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG, result.toString()
			};

			ProjectTest.checkProjectOutput("Search Index/Simple", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testSearchIndexAlphabet() {
			String name = "search-index-alpha.txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR);
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "alphabet.txt");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
			Path result = Paths.get(ProjectTest.RESULT_DIR, name);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG, result.toString()
			};

			ProjectTest.checkProjectOutput("Search Index/Alpha", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testSearchIndexComplex() {
			String name = "search-index-complex.txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR);
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "complex.txt");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, name);
			Path result = Paths.get(ProjectTest.RESULT_DIR, name);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG, result.toString()
			};

			ProjectTest.checkProjectOutput("Search Index/Complex", args, result, output);
		}
	}
}
