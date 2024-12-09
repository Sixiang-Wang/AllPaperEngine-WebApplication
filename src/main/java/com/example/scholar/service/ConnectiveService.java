package com.example.scholar.service;

import com.example.scholar.domain.User;
import com.example.scholar.domain.openalexElasticsearch.Works;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ConnectiveService {
    List<String> sortKeywords();
    List<Works> getWorksByKeyword(String keyword);
}
