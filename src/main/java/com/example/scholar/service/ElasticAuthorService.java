package com.example.scholar.service;

import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ElasticAuthorService {

    List<SearchHit> searchByDisplayNameByPage(String displayName, int page);
    List<SearchHit> searchByDisplayName(String displayName, int page);
}
