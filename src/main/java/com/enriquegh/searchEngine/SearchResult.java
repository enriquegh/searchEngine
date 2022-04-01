package com.enriquegh.searchEngine;

/**
 * This class is an object that contains the necessary elements of a search
 * results hit.
 *
 * The class implements Comparable so it may be able to sort all the instances.
 *
 */
public class SearchResult implements Comparable<SearchResult> {

    private int frequency;
    private int position;
    private final String path;

    /**
     * Stores frequency, position and path to the SearchResult instance.
     *
     * @param frequency
     * @param position
     * @param path
     */
    public SearchResult(int frequency, int position, String path) {
        this.frequency = frequency;
        this.position = position;
        this.path = path;
    }

    /**
     * Compares two SearchResult instances.<br>
     * First compares by frequency, if equal then compares by the relative
     * position of each result and if that still isn't enough it compares by
     * their path.
     *
     * @param o
     *            SearchResult that will be compared.
     *
     */
    @Override
    public int compareTo(SearchResult o) {

        if (Integer.compare(o.frequency, this.frequency) != 0) {
            return Integer.compare(o.frequency, this.frequency);
        } else {
            if (Integer.compare(this.position, o.position) != 0) {
                return Integer.compare(this.position, o.position);
            } else {
                return path.compareToIgnoreCase(o.path);
            }
        }
    }

    @Override
    public String toString() {
        return "\"" + path + "\", " + frequency + ", " + position;
    }

    /**
     * Updates the frequency by adding wordAppeared.
     *
     * @param wordAppeared
     *            Number that will be added to frequency.
     */
    public void updateFrequency(int wordAppeared) {
        frequency += wordAppeared;
    }

    /**
     * Checks if position stored is less than position given. If it is, it saves
     * the new lowest value to position variable.
     *
     * @param num
     *            Number to be compared.
     */
    public void checkPositions(Integer num) {
        if (Integer.compare(num, this.position) < 0) {
            this.position = num;
        }
    }

    public String getPath() {
    	return path;
    }

}
