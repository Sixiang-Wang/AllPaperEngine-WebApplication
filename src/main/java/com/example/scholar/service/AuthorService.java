package com.example.scholar.service;

import com.example.scholar.domain.openalex.AuthorShips;
import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.AuthorResultDto;
import com.example.scholar.dto.WorkAuthorResultDto;

import java.util.ArrayList;
import java.util.List;

public interface AuthorService {
    ArrayList<WorkAuthorResultDto> getAuthorsByWorkId(String workId);


    List<Work> getWorksByAuthorId(String authorId);
    int getWorksCountByAuthorId(String authorId);
    List<Work> getHighQualityWorksByAuthorId(String authorId);
    int getHighQualityWorksCountByAuthorId(String authorId,boolean track);
    int getCitedCountByAuthorId(String authorId);
    int getHNumberByAuthorId(String authorId,boolean track);
    List<Work> getFirstPublishWorkByAuthorId(String authorId);
    int getFirstPublishWorkCountByAuthorId(String authorId,boolean track);
}
