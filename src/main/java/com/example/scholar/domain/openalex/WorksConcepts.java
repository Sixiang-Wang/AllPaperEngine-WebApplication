package com.example.scholar.domain.openalex;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Transient;

@Component
@Data
public class WorksConcepts {
    private String workId;
    private String conceptId;
    private Float score;
    @Transient
    private String displayName;
//    private String wikidata;
}
