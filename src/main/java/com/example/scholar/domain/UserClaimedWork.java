package com.example.scholar.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserClaimedWork {
    private int id;
    private int userId;
    private String workId;
    private Timestamp claimTime;
    private int available;
}
