import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO Mix of tabs and spaces

public class ThreadSafeInvertedIndex extends InvertedIndex {
    private static Logger logger = LogManager.getLogger();
    private final ReadWriteLock lock;

    public ThreadSafeInvertedIndex() {
        super();
        lock = new ReadWriteLock();
    }

    @Override
    public boolean add(String word, String path, int position) {
        lock.lockWrite();
        boolean add = super.add(word, path, position);
        lock.unlockWrite();
        return add;
    }

    // TODO Potential issue, but do not need to fix.
    @Override
    public boolean addAll(InvertedIndex tempIndex) {
        lock.lockWrite();
        boolean add = super.addAll(tempIndex);
        lock.unlockWrite();
        return add;
    }

    // TODO Override contains() and toString()

    @Override
    public void print(String output) throws IOException {
        lock.lockRead();
        super.print(output);
        lock.unlockRead();
    }

    @Override
    public ArrayList<SearchResult> search(String[] queryList) {
        lock.lockRead();
        logger.debug("I am inside search");
        ArrayList<SearchResult> search = super.search(queryList);
        lock.unlockRead();
        return search;
    }
}
