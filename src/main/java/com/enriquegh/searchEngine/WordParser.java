package com.enriquegh.searchEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * This class parses through text removing unnecessary spaces and special
 * characters and assumes words will be separated by whitespace.
 */
public class WordParser {

    /**
     * Converts text into a consistent format by converting text to lower-case,
     * replacing non-word characters and underscores with a single space, and
     * finally removing leading and trailing whitespace. (See the {@link String}
     * class for several helpful methods.)
     *
     * @param text
     *            - original text
     * @return text without special characters and leading or trailing spaces
     */
    public static String cleanText(String text) {

        text = text.toLowerCase();
        text = text.replaceAll("[\\W_]+", " ");
        text = text.trim();

        return text;
    }

    /**
     * Splits text into words by whitespaces, cleans the resulting words using
     * {@link #cleanText(String)} so that they are in a consistent format, and
     * adds non-empty words to an {@link ArrayList}.
     *
     *
     * @param text
     *            - original text
     * @return list of cleaned words
     */
    public static List<String> parseText(String text) {
        ArrayList<String> words = new ArrayList<>();
        String[] wordsString = cleanText(text).split(" ");

        for (String word : wordsString) {

            if (!word.isEmpty()) {
                words.add(word);
            }
        }
        return words;
    }
}
