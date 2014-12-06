import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadSafeQueryParser extends QueryParser {
    private static Logger logger = LogManager.getLogger();

    private final WorkQueue workers;
    private final ReadWriteLock lock;
    private int pending;

    public ThreadSafeQueryParser() {
        super();
        workers = new WorkQueue();
        lock = new ReadWriteLock();
    }

    public ThreadSafeQueryParser(int threads) {
        super();
        workers = new WorkQueue(threads);
        lock = new ReadWriteLock();
        
    }
    

    @Override
    public void addResult(String line, java.util.ArrayList<SearchResult> tempResults) {
        lock.lockWrite();
        super.addResult(line, tempResults);
        lock.unlockWrite();
    }
    /**
     * This method 
     * @param file
     * @param index
     * @throws IOException
     */

    public void parseFile(Path file, ThreadSafeInvertedIndex index) throws IOException {

        try (BufferedReader reader = Files.newBufferedReader(file,
                Charset.forName("UTF-8"));) {
            String line = null;

            while ((line = reader.readLine()) != null) {
                ArrayList<SearchResult> emptyResults = new ArrayList<SearchResult>();
                addResult(line, emptyResults);
                workers.execute(new LineWorker(line, index));
            }
        }
    }

    private class LineWorker implements Runnable {
        
        private final String line;
        private final ThreadSafeInvertedIndex index;

        public LineWorker(String line, ThreadSafeInvertedIndex index) {
            this.line = line;
            this.index = index;
            incrementPending();
        }

        @Override
        public void run() {
            String[] wordsString = line.split(" ");
            ArrayList<SearchResult> searchResults = index.search(wordsString);
            addResult(line, searchResults);
            decrementPending();

        }
    }

    /**
     * Indicates that we now have additional "pending" work to wait for. We need
     * this since we can no longer call join() on the threads. (The threads keep
     * running forever in the background.)
     *
     * We made this a synchronized method in the outer class, since locking on
     * the "this" object within an inner class does not work.
     */
    private synchronized void incrementPending() {
        pending++;
        logger.debug("Pending is now {}", pending);
    }

    /**
     * Indicates that we now have one less "pending" work, and will notify any
     * waiting threads if we no longer have any more pending work left.
     */
    private synchronized void decrementPending() {
        pending--;
        logger.debug("Pending is now {}", pending);

        if (pending <= 0) {
            this.notifyAll();
        }
    }

    /**
     * Helper method, that helps a thread wait until all of the current work is
     * done. This is useful for resetting the counters or shutting down the work
     * queue.
     */
    public synchronized void finish() {
        try {
            while (pending > 0) {
                logger.debug("Waiting until finished");
                this.wait();
            }
        } catch (InterruptedException e) {
            logger.debug("Finish interrupted", e);
        }
    }

    /**
     * Will shutdown the work queue after all the current pending work is
     * finished. Necessary to prevent our code from running forever in the
     * background.
     */
    public synchronized void shutdown() {
        logger.debug("Shutting down");
        finish();
        workers.shutdown();
    }

}
