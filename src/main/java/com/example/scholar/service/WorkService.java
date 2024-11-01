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
    List<WorkResultDto> getWorksByTitleWords(String word,int page);
    String ToMainInformation(Work work);
    List<WorkResultDto> getWorksByPublicationYear(int from,int to,int page);
    List<WorkResultDto> getWorkByTitleAndPublicationYear(String word,int from,int to,int page);
    List<WorkResultDto> getWorkByKeywords(String word,int page);
}
