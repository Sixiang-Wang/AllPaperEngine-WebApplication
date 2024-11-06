package com.example.scholar.service;

import com.example.scholar.domain.openalex.Work;

import java.util.List;

public interface ClaimWorkService {
    int claimWork(int scholarId, String workId);
    int deleteClaimedWork(int scholarId, String workId);
    List<Work> selectClaimedWorks(int scholarId);
}
