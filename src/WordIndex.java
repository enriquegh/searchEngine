import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class WordIndex {

    /**
     * Stores a mapping of words to the positions those words were found.
     */
    private final Map<String, Map<String, Set<Integer>>> wordMap;

    /**
     * Properly initializes the index. Choose the fastest data structures
     * available, as sorting is not a requirement of this index.
     */
    public WordIndex() {
        wordMap = new TreeMap<String, Map<String, Set<Integer>>>();

    }

    /**
     * Properly adds a word and position to the index. Must initialize inner
     * data structure if necessary. Make sure you consider how to handle
     * duplicates (duplicate words, and words with duplicate positions).
     *
     * @param word
     *            - word to add to index
     * @param position
     *            - position word was found
     * @return true if this was a unique entry, false if no changes were made to
     *         the index
     */
    public boolean add(String word, String path, int position) {

        Set<Integer> set;
        if (!wordMap.containsKey(word)) {

            Map<String, Set<Integer>> pathMap = new TreeMap<>();
            set = new TreeSet<>();

            wordMap.put(word, pathMap);
            pathMap.put(path, set);
            set.add(Integer.valueOf(position));

            return true;

        } else {

            if (!wordMap.get(word).containsKey(path)) { // TODO Just test
                                                        // !wordMap.get(word).containsKey(path)
                set = new TreeSet<Integer>();
                set.add(position);
                wordMap.get(word).put(path, set);

                return true;
            }

            else if (!wordMap.get(word).get(path).contains(position)) {

                wordMap.get(word).get(path).add(position);
                return true;
            }

            else {
                return false;
            }

        }

    }

    /**
     * Returns the number of times a word was found (i.e. the number of
     * positions associated with a word in the index).
     *
     * @param word
     *            - word to look for
     * @return number of times word was found
     */

    /**
     * Returns the total number of words stored in the index.
     * 
     * @return number of words
     */
    public int words() {
        return wordMap.size();
    }

    /**
     * Tests whether the index contains the specified word.
     * 
     * @param word
     *            - word to look for
     * @return true if the word is stored in the index
     */
    public boolean contains(String word) {
        return wordMap.containsKey(word);
    }

    /**
     * Safely returns the set of positions for a specified word (or an empty set
     * if the word is not found). Be wary of directly returning a reference to
     * your private mutable data!
     *
     * @param word
     *            - word to look for
     * @return set of positions associated with word (will be empty if word was
     *         not found)
     * @throws IOException
     */

    public void print(String output) throws IOException {

        Path outputPath = Paths.get(output);

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath,
                Charset.forName("UTF-8"));) {
            
            for (Entry<String, Map<String, Set<Integer>>> word : wordMap
                    .entrySet()) {

                writer.write(word.getKey());
                writer.newLine();

                for (Entry<String, Set<Integer>> location : word.getValue()
                        .entrySet()) {

                    writer.write("\"" + location.getKey() + "\"");

                    for (Integer position : location.getValue()) {
                        writer.write(", " + position);
                    }

                    writer.newLine();

                }
                writer.newLine();
                writer.flush();

            }

        }

    }

    /**
     * Returns a string representation of this index for debugging.
     */
    @Override
    public String toString() {

        return wordMap.toString();
    }
}
