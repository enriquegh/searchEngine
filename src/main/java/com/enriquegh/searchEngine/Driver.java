package com.enriquegh.searchEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * This class runs all the methods and classes needed to make search engine
 * work. Driver calls {@link ArgumentParser} & {@link InvertedIndex}. Checks
 * that the flag -d and a directory are entered. If flag -i is entered an output
 * directory will be included. If flag is included but no directory it will be
 * saved to "index.txt"
 *
 * Example run config: -u http://logging.apache.org/log4j/1.2/apidocs/allclasses-noframe.html -t 5 -p
 */

@SpringBootApplication
public class Driver {
    private static Logger logger = LogManager.getLogger();


    public static void main(String[] args) {
        String outputPath;
        int threads;
        ArgumentParser parser = new ArgumentParser(args);

//        ThreadSafeInvertedIndex index = new ThreadSafeInvertedIndex(); //Need to call here if not traverse uses parent method

        if (parser.hasValue("-t")) {

            try {
                threads = Integer.parseInt(parser.getValue("-t"));
            } catch (NumberFormatException e) {
                threads = 5;
            }
        } else {
            threads = 5;
        }
        ThreadSafeInvertedIndexBuilder tsbuilder;
        ThreadSafeQueryParser parseQuery;
        tsbuilder = new ThreadSafeInvertedIndexBuilder(threads);
        parseQuery = new ThreadSafeQueryParser(threads);

//        if (parser.hasFlag("-u") && parser.hasValue("-u")) {
//            LinkTraverser traverser = new LinkTraverser(threads);
//            String urlPath = parser.getValue("-u");
//
//            traverser.traverse(urlPath, index);
//            logger.debug("Finished traversing links");
//            traverser.shutdown();
//
//        }

        if (parser.hasFlag("-p")) {
            int port;

            if (parser.hasValue("-p")) {

                try {
                    port = Integer.parseInt(parser.getValue("-p"));
                } catch (NumberFormatException e) {
                    port = 8080;
                }
            } else {
                port = 8080;
            }

//                logger.info("Server not working right now...");
            SpringApplication.run(Driver.class, args);
        }

        tsbuilder.shutdown();
        parseQuery.shutdown();

    }

}
