package com.example.scholar.domain;

import com.example.scholar.domain.openalex.Work;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class UserClaimedWork {
    private int scholarId;
    private String workId;
    private Timestamp claimTime;
}
