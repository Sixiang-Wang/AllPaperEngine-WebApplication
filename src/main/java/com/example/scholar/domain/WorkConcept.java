package com.example.scholar.domain;

import com.example.scholar.domain.openalex.Concepts;
import com.example.scholar.domain.openalex.Work;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class WorkConcept {
    public String workId;
    public String conceptId;
    public double score;
    public Concepts concept;
    public Work work;
}
