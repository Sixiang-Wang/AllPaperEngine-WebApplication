package com.example.scholar.domain.openalex;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Component
@Data
public class KeywordWorkPair {
    private String workId;
    private String workTitle;
    private int publicationYear;
    private Time publicationDate;
    private int citedByCount;
    private String keywordId;
    private String keywordDisplayName;
    private double score;
}
