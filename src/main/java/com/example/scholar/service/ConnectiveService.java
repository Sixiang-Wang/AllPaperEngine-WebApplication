package com.example.scholar.service;

import com.example.scholar.domain.User;
import com.example.scholar.domain.openalexElasticsearch.Works;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ConnectiveService {
    List<String> sortKeywords();
    List<Integer> sortPublictionYears();
    List<String> sortTypes();
    List<String> sortLanguages();

    List<Works> getWorksByKeyword(String keyword);
    List<Works> getWorksByPublicationYear(int publicationYear);
    List<Works> getWorksByType(String type);
    List<Works> getWorksByLanguage(String language);

}
