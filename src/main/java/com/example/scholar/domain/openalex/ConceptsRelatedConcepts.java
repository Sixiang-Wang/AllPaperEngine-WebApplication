package com.example.scholar.domain.openalex;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ConceptsRelatedConcepts {
    private String conceptId;
    private String relatedConceptId;
    private Float score;
}
