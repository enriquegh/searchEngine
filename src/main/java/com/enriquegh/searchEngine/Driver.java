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

        SpringApplication.run(Driver.class, args);


    }

}
