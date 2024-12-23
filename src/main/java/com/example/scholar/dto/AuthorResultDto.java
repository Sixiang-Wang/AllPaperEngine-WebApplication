package com.example.scholar.dto;

import lombok.Data;

import java.util.ArrayList;
@Data
public class AuthorResultDto {
    private String authorId;
    private String authorName;
    private int worksCount;
    private int citedByCount;
    private String worksApiUrl;
}
