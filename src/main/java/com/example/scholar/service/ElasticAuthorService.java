package com.example.scholar.service;

import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Component;

@Component
public interface ElasticAuthorService {

    SearchHits searchByDisplayNameByPage(String displayName);
    SearchHits searchByDisplayName(String displayName);
}
