
// TODO Add javadoc

public class SearchResult implements Comparable<SearchResult> {

    private int frequency;
    private int position;
    private final String path;

    
    public SearchResult(int frequency, int position, String path) {
        this.frequency = frequency;
        this.position = position;
        this.path = path;
    }
    
    @Override
    public int compareTo(SearchResult o) {
        
        if (Integer.compare(o.frequency, this.frequency) != 0) {
            return Integer.compare(o.frequency, this.frequency);
        }
        else {
            if (Integer.compare(this.position,o.position) != 0) {
                return Integer.compare(this.position,o.position);
            }
            else {
                return path.compareToIgnoreCase(o.path);
            }
        }
    }
    
    @Override
    public String toString() {
        return "\"" + path + "\", " + frequency + ", " + position;
    }

    public boolean hasPath(String key) {
        return path.equalsIgnoreCase(key);
    }

    public void updateFrequency(int wordAppeared) {
        frequency += wordAppeared;
    }

    public void checkPositions(Integer num) {
        if (Integer.compare(num, this.position) < 0) {
            this.position = num;
        }
    }

}
