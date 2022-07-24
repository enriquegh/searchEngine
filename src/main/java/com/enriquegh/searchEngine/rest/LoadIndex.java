package com.enriquegh.searchEngine.rest;

import com.enriquegh.searchEngine.LinkTraverser;
import com.enriquegh.searchEngine.ThreadSafeInvertedIndex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadIndex {

    @Bean
    public ThreadSafeInvertedIndex initTSIndex()
    {
        ThreadSafeInvertedIndex index = new ThreadSafeInvertedIndex();
        return index;
    }

    @Bean
    public LinkTraverser initTravserser(ThreadSafeInvertedIndex index)
    {

        int threads = 5;
        LinkTraverser traverser = new LinkTraverser(threads);
        String urlPath = "http://logging.apache.org/log4j/1.2/apidocs/allclasses-noframe.html";

        traverser.traverse(urlPath, index);
        traverser.shutdown();

        return traverser;
    }
}
