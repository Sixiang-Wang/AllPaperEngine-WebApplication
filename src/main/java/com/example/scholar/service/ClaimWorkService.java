package com.example.scholar.service;

import com.example.scholar.domain.openalex.Work;
import com.example.scholar.dto.WorkResultDto;

import java.util.List;

public interface ClaimWorkService {
    int claimWork(int scholarId, String workId);
    int deleteClaimedWork(int scholarId, String workId);
    List<WorkResultDto> selectClaimedWorks(int scholarId);
}
