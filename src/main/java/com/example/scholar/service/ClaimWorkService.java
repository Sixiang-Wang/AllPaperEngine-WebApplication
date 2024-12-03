package com.example.scholar.service;

import com.example.scholar.dto.ClaimResultDto;
import com.example.scholar.dto.WorkResultDto;

import java.util.List;

public interface ClaimWorkService {
    int claimWork(int scholarId, String workId);
    int deleteClaimedWork(int id);
    List<ClaimResultDto> allClaimUnavailable();
    List<WorkResultDto> selectClaimedWorks(int scholarId);

    int ableClaim(int id);
    int disableClaim(int id);
}
