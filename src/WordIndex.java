import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class WordIndex {

    /**
     * Stores a mapping of words to the path and position those words were found.
     */
    private final Map<String, Map<String, Set<Integer>>> wordMap;

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
     * @param path
     *            - path where word was found
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

    
    public boolean addAll(List<String> list, String file, int i){
        for (String word : list){
            add(word,file, i);
            i++;
        }
        return true;
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
     * @param output
     *            - path where the word, path and position will be stored in
     * @return
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
