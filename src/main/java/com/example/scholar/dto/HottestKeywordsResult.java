package com.example.scholar.dto;


import lombok.Data;

@Data
public class HottestKeywordsResult {
    private String id;
    private String displayName;
    private double totalScore;
    private int rank;//排名
}
