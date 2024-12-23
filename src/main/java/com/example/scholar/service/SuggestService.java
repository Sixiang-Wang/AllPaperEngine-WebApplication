package com.example.scholar.service;

import com.example.scholar.dto.SuggestDto;
import com.example.scholar.dto.WorkResultDto;

import java.util.List;

public interface SuggestService {
    List<SuggestDto> getSuggestWorks(String workId);


}
