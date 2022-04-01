package com.enriquegh.searchEngine;

import java.util.List;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * This class is called for parsing a URL given on Driver.
 * Parses through link fetching HTML and traversing over more
 * links until it reaches numLinks.
 *
 */
public class LinkTraverser {
	private static Logger logger = LogManager.getLogger();
	private final WorkQueue workers;
	private int pending;
	private TreeSet<String> listLinks;
	private final ReadWriteLock lock;
	private final int NUMLINKS;

	/**
	 * Initializes all variables necessary for class assuming user
	 * didn't enter a number for amount of threads.
	 */
	public LinkTraverser() {
		workers = new WorkQueue();
		listLinks = new TreeSet<String>();
		lock = new ReadWriteLock();
		NUMLINKS = 50;
	}
	/**
	 * Initializes all variables necessary for class
	 *
	 * @param threads - number of threads that will be used.
	 */
	public LinkTraverser(int threads) {
		workers = new WorkQueue(threads);
		listLinks = new TreeSet<String>();
		lock = new ReadWriteLock();
		NUMLINKS = 50;
	}

	/**
	 * Adds seed URL to list and starts the LinkWorker class
	 *
	 * @param urlPath - Seed url
	 * @param index - Main index initialized in Driver
	 */
	public void traverse(String urlPath, ThreadSafeInvertedIndex index) {
		lock.lockWrite();
		listLinks.add(urlPath); // Seed is added
		lock.unlockWrite();
		workers.execute(new LinkWorker(urlPath, index));
	}

	/**
	 * This class is in charge of running the Workers that will parse
	 * a path, put the words into the InvertedIndex and call another worker
	 * if a link is found inside this path.
	 *
	 */
	private class LinkWorker implements Runnable {
		private final String urlPath;
		private final ThreadSafeInvertedIndex mainIndex;
		private final InvertedIndex tempIndex;

		/**
		 * Initializes indexes and paths that will be used to parse link.
		 * @param urlPath path that will be parsed
		 * @param index - Main multithreaded index
		 */
		public LinkWorker(String urlPath, ThreadSafeInvertedIndex index) {
			logger.debug("Worker created for {}", urlPath);
			this.urlPath = urlPath;
			this.mainIndex = index;
			tempIndex = new InvertedIndex();
			incrementPending();

		}

		@Override
		public void run() {

			String html = HTTPFetcher.fetchHTML(urlPath);
			lock.lockWrite();
			for (String link : HTMLLinkParser.listLinks(html, urlPath)) {
				if (!listLinks.contains(link) && listLinks.size() < NUMLINKS) {
					listLinks.add(link);
					logger.debug("Worker will be executed");
					workers.execute(new LinkWorker(link, mainIndex));
				}
			}
			lock.unlockWrite();

			String text = HTMLCleaner.cleanHTML(html);
			int i = 1;
			logger.debug("Path {} will be parsed", urlPath);
			List<String> words = WordParser.parseText(text);
			for (String word : words) {
				mainIndex.add(word, urlPath, i);
				i++;
			}

			mainIndex.addAll(tempIndex);
			decrementPending();
			logger.debug("Worker finished {}", urlPath);
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
