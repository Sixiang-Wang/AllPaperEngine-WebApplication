package com.example.scholar.dto;

import lombok.Data;

import java.util.ArrayList;
@Data
public class AuthorResultDto {
    private String authorId;
    private ArrayList<String> authorName;
    private int works_count;
    private int cited_by_count;
    private String works_api_url;
}
