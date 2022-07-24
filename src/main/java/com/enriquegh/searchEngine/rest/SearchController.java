package com.enriquegh.searchEngine.rest;

import com.enriquegh.searchEngine.SearchResult;
import com.enriquegh.searchEngine.ThreadSafeInvertedIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {
    private final SearchRepository repository;

    @Autowired
    private ThreadSafeInvertedIndex index;

    SearchController(SearchRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/search")
    List<SearchResult> search(@RequestParam String query) {
        return index.search(query.trim().split(" "));
    }
}
