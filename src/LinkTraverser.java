import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LinkTraverser {
    private static Logger logger = LogManager.getLogger();
    private final WorkQueue workers;
    private int pending;
    private static int urlCount = 0;
    
    public LinkTraverser(){
        workers = new WorkQueue();
    }
    
    public LinkTraverser(int threads) {
        workers = new WorkQueue(threads);
    }
    
    public void traverse(String urlPath, ThreadSafeInvertedIndex index) {
        String html;
        try {
            html = HTTPFetcher.fetchHTML(urlPath);
            ArrayList<String> links = HTMLLinkParser.listLinks(html);
            for (String link :links) {
                if (urlCount <= 50) {
                    logger.debug("Worker will be executed");
                    workers.execute(new LinkWorker(link,index));
                    urlCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void parsePath(String urlPath, InvertedIndex index) {
        try {
            String html = HTTPFetcher.fetchHTML(urlPath);
            String text = HTMLCleaner.cleanHTML(html);
            String[] textWords = text.split(" ");
            int i = 1;
            logger.debug("Path {} will be parsed",urlPath);
            for (String word : textWords) {
                index.add(word,urlPath, i);
                i++;
            }
        }
        catch (IOException e) {
            logger.debug(e);
        }
    }
    
    public static void printLinks(ArrayList<String> links) {
        for (String url : links) {
            System.out.println(url);
        }
    }
    
    private class LinkWorker implements Runnable {
        private final String urlPath;
        private final ThreadSafeInvertedIndex mainIndex;
        private final InvertedIndex tempIndex;
        public LinkWorker(String urlPath, ThreadSafeInvertedIndex index) {
            // TODO Auto-generated constructor stub
            logger.debug("Worker created for {}", urlPath);
            this.urlPath = urlPath;
            this.mainIndex = index;
            tempIndex = new InvertedIndex();
            incrementPending();
            
        }
        @Override
        public void run() {
            parsePath(urlPath,tempIndex);
            decrementPending();
            mainIndex.addAll(tempIndex);
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
//        logger.debug("Pending is now {}", pending);
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
//                logger.debug("Waiting until finished");
                this.wait();
            }
        } catch (InterruptedException e) {
//            logger.debug("Finish interrupted", e);
        }
    }

    /**
     * Will shutdown the work queue after all the current pending work is
     * finished. Necessary to prevent our code from running forever in the
     * background.
     */
    public synchronized void shutdown() {
//        logger.debug("Shutting down");
        finish();
        workers.shutdown();
    }


}
