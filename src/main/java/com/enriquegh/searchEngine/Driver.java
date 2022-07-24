package com.enriquegh.searchEngine;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Driver {
    private static Logger logger = LogManager.getLogger();


    public static void main(String[] args) {

        logger.info("Starting Spring Application...");
        SpringApplication.run(Driver.class, args);


    }
}
