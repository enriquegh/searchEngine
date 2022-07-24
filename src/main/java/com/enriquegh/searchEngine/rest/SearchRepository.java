package com.enriquegh.searchEngine.rest;

import com.enriquegh.searchEngine.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;

interface SearchRepository extends JpaRepository<SearchResult, Long> {

}
