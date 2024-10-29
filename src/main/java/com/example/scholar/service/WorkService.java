package com.example.scholar.service;

import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.WorkResultDto;
import com.example.scholar.dto.WorkSpecificResultDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WorkService {
    List<WorkResultDto> getWorks();
    List<WorkResultDto> getWorksByPage(int page);
    WorkSpecificResultDto getWorkById(String workId);
    List<WorkResultDto> getWorksByTitleWords(String word);
    String ToMainInformation(Work work);
}
