package com.example.scholar.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClaimResultDto {
    private int id;
    private int userId;
    private String name;
    private String nameReal;
    private String title;
    private List<String> AuthorList;
}
