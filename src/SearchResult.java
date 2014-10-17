
public class SearchResult implements Comparable<SearchResult> {

    private int frequency;
    private int position;
    private String path;

    
    SearchResult(int frequency, int position, String path){
        this.frequency = frequency;
        this.position = position;
        this.path = path;
    }
    
    @Override
    public int compareTo(SearchResult o) {
        // TODO compareTo frequency > postion > location
        
        if (Integer.compare(this.frequency, o.frequency) != 0){
            return Integer.compare(this.frequency, o.frequency);
        }
        
        else{
            if (Integer.compare(this.position,o.position) != 0){
                return Integer.compare(this.position,o.position);
            }
            else{
                return path.compareToIgnoreCase(o.path);
            }
        }
    }
    
    @Override
    public String toString() {
        
        return "\"" + path + "\", " + frequency + ", " + position;
    }

}
