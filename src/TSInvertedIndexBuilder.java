import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class TSInvertedIndexBuilder extends InvertedIndexBuilder {
    private static Logger logger = LogManager.getLogger();
    
    // TODO Have a constructor, initialize non-static members there.
    private final WorkQueue workers = new WorkQueue();
    private int pending;

    // TODO Add javadoc
    public void traverse(Path path, TSInvertedIndex index) throws IOException {

        try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {

            for (Path file : listing) {
                String fileName = file.getFileName().toString().toLowerCase();

                    if (fileName.endsWith(".txt")) {
                        workers.execute(new DirectoryWorker(file,index));
                    }
                    else if(Files.isDirectory(file)) {
                        traverse(file,index);
                    }

            }
        }
    }
    
    /**
     * Handles per-directory parsing. If a subdirectory is encountered, a new
     * {@link DirectoryMinion} is created to handle that subdirectory.
     */
    private class DirectoryWorker implements Runnable {
        // TODO Use final where appropriate
        private Path directory;
        private TSInvertedIndex mainIndex;
        // TODO Initialize in constructor
        private InvertedIndex tempIndex = new InvertedIndex();

        public DirectoryWorker(Path directory, TSInvertedIndex index) {
            logger.debug("Worker created for {}", directory);
            this.directory = directory;
            this.mainIndex = index;
            // Indicate we now have "pending" work to do. This is necessary
            // so we know when our threads are "done", since we can no longer
            // call the join() method on them.
            incrementPending();
        }

        @Override
        public void run() {
            try {
                parseFile(directory, tempIndex);
            } catch (IOException e) {
                e.printStackTrace(); // TODO No printing of stack trace
            }

            // Indicate that we no longer have "pending" work to do.
            mainIndex.addAll(tempIndex);
            decrementPending();
//                mainIndex.addAll(list, file, start)
            logger.debug("Worker finished {}", directory);
        }
    }



    /**
     * Indicates that we now have additional "pending" work to wait for. We
     * need this since we can no longer call join() on the threads. (The
     * threads keep running forever in the background.)
     *
     * We made this a synchronized method in the outer class, since locking
     * on the "this" object within an inner class does not work.
     */
    private synchronized void incrementPending() {
        pending++;
        logger.debug("Pending is now {}", pending);
    }

    /**
     * Indicates that we now have one less "pending" work, and will notify
     * any waiting threads if we no longer have any more pending work left.
     */
    private synchronized void decrementPending() {
        pending--;
        logger.debug("Pending is now {}", pending);

        if (pending <= 0) {
            this.notifyAll();
        }
    }
    
    /**
     * Helper method, that helps a thread wait until all of the current
     * work is done. This is useful for resetting the counters or shutting
     * down the work queue.
     */
    public synchronized void finish() {
        try {
            while (pending > 0) {
                logger.debug("Waiting until finished");
                this.wait();
            }
        }
        catch (InterruptedException e) {
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
    
    // TODO Shouldn't need this
    public static void parseFile(Path file, InvertedIndex index) throws IOException {
        
        try (BufferedReader reader = Files.newBufferedReader(file,
                Charset.forName("UTF-8"));)
        {
            String line = null;
            int i = 1;
            
            while ((line = reader.readLine()) != null) {
                String[] wordsString = WordParser.cleanText(line).split(" ");
                
                for (String word : wordsString) {

                    if (!word.isEmpty()) {
                        index.add(word, file.toString(), i);
                        i++;
                    }
                }
            }
        } 
    }
    
    
    
}


