import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InvertedIndex {
    private static Logger logger = LogManager.getLogger();
    private final TreeMap<String, Map<String, TreeSet<Integer>>> wordMap;
    /**
     * Stores a mapping of words to the path and position those words were found.
     */
    public InvertedIndex() {
        wordMap = new TreeMap<String, Map<String, TreeSet<Integer>>>();
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
        TreeSet<Integer> set;
        
        if (!wordMap.containsKey(word)) {
            Map<String, TreeSet<Integer>> pathMap = new TreeMap<>();
            set = new TreeSet<>();

            wordMap.put(word, pathMap);
            pathMap.put(path, set);
            set.add(Integer.valueOf(position));

            return true;
        } else {
            if (!wordMap.get(word).containsKey(path)) {
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
     * Adds a whole List of type String into the WordIndex
     * 
     * @param list
     *            - list to be added
     * @param file
     *            - String path to where word was found
     * @param start
     *            - Number where the position should start
     * @return true if the word is stored in the index
     */
    public boolean addAll(List<String> list, String file, int start) {
        for (String word : list) {
            add(word,file, start);
            start++;
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
        if (!outputPath.toFile().isDirectory()) {
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath,
                    Charset.forName("UTF-8"));) {
                
                for (Entry<String, Map<String, TreeSet<Integer>>> word : wordMap
                        .entrySet()) {
                    writer.write(word.getKey());
                    writer.newLine();
    
                    for (Entry<String, TreeSet<Integer>> location : word.getValue()
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

    }
    

    /**
     * Traverses through query and searches the wordMap
     * for possible matches. All matches are saved into a
     * SearchResult instance.
     *
     * @param queryList
     *            - String array that contains all the words on the query
     * @return ArrayList<SearchResult>
     *            - ArrayList with SearchResult instances
     */
    public ArrayList<SearchResult> search(String[] queryList) {
        ArrayList<SearchResult> searchResultsList = new ArrayList<>();
        Map<String, SearchResult> searchResultsMap = new HashMap<>();
        
        for (String query : queryList) {
            
            for (Entry<String, Map<String, TreeSet<Integer>>> word :  wordMap.tailMap(query).entrySet()) {
                
                if (!word.getKey().startsWith(query)) {
                    break;
                }

                for (Entry<String, TreeSet<Integer>> path : word.getValue().entrySet()) {          
                    int wordAppeared = path.getValue().size();
                    
                    if (searchResultsMap.containsKey(path.getKey())) {
                        searchResultsMap.get(path.getKey()).updateFrequency(wordAppeared);
                        searchResultsMap.get(path.getKey()).checkPositions(path.getValue().first());
                    }
                    else {
                        SearchResult wordSearched = new SearchResult(wordAppeared, path.getValue().first(), path.getKey());
                        searchResultsMap.put(path.getKey(), wordSearched);
                    }
                }
            }
        }
        searchResultsList.addAll(searchResultsMap.values());
        Collections.sort(searchResultsList);

        return searchResultsList;
    }

    /**
     * Returns a string representation of this index for debugging.
     */
    @Override
    public String toString() {
        return wordMap.toString();
    }
}
