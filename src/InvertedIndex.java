import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO Formatting and Javadoc. Pay specific attention to your use of blank lines and spaces.

public class InvertedIndex {
    private static Logger logger = LogManager.getLogger();
    /**
     * Stores a mapping of words to the path and position those words were found.
     */
    private final TreeMap<String, Map<String, TreeSet<Integer>>> wordMap;

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

    public void printInvertedIndex(String output) throws IOException { // TODO Rename this to print()

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
    
    // TODO Move this to QueryParser, create the LinkedHashMap as a member there.
    public void printQueryResults(LinkedHashMap<String, ArrayList<SearchResult>> searchResultList, String output) throws IOException{
        Path outputPath = Paths.get(output);
        if (!outputPath.toFile().isDirectory()) {
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath,
                    Charset.forName("UTF-8"));) {
                for (Entry<String, ArrayList<SearchResult>> queryResult : searchResultList.entrySet()){
                    writer.write(queryResult.getKey());
                    writer.newLine();
                    for (SearchResult sr : queryResult.getValue()){
                        writer.write(sr.toString());
                        writer.newLine();
                    }
                    writer.newLine();
                }
                writer.flush();
    
            }
        }
    }
    
    //TODO search function on InvertedIndex Function
    public ArrayList<SearchResult> search(String[] queryList) {

        ArrayList<SearchResult> searchResultsList = new ArrayList<>();
        // TODO Map<String, SearchResult> searchResultMap = new HashMap<>(); (key is path, value is result for that path)
        
        for (String query : queryList) {
            for (Entry<String, Map<String, TreeSet<Integer>>> word :  wordMap.tailMap(query).entrySet()) {
                
                if (!word.getKey().startsWith(query)) {
                    break;
                }
                //count++;
                // TODO Traverse through each path of word look up position
                // Need to check and updated initial position and count.
                for (Entry<String, TreeSet<Integer>> path : word.getValue().entrySet()){
                    int wordAppeared = path.getValue().size();
                    
                    /* TODO
                     * Instead of the code below, check if the map has the path. If it does
                     * update the search result object stored in the map. Otherwise add
                     * a new search result object.
                     */
                    
                    
                    if (searchResultsList.isEmpty()){
                        SearchResult wordSearched = new SearchResult(wordAppeared, path.getValue().first(), path.getKey());
                        searchResultsList.add(wordSearched);
                    }
                    
                    else{
                        boolean updatedSearch = false;
                        
                        // TODO Linear search, whenever you do this, you know you are using the wrong data structure
                        for (SearchResult searchResults : searchResultsList){
                            if (searchResults.hasPath(path.getKey())){
                                searchResults.updateFrequency(wordAppeared);
                                searchResults.checkPositions(path.getValue().first());
                                updatedSearch = true;
                                break;
                            }
                            
                        }
                        if (!updatedSearch){
                            SearchResult wordSearched = new SearchResult(wordAppeared, path.getValue().first(), path.getKey());
                            searchResultsList.add(wordSearched);
                        }
                        
                    }
                }

               
            }

            //logger.debug(searchResultsList);
            
        }
        
        // TODO searchResultsList.addAll(searchResultsMap.values());
        
        Collections.sort(searchResultsList);
        logger.debug(searchResultsList);
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
