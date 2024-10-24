package com.example.scholar.service;

import com.example.scholar.dto.ConceptsResultDto;

import java.util.List;

public interface ConceptsService {
    List<ConceptsResultDto> getConceptsByWorkId(String workId);


}
