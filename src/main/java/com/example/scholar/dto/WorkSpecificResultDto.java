package com.example.scholar.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

@Data
public class WorkSpecificResultDto {
    private ArrayList<WorkAuthorResultDto> workAuthorResultDtos;
    private String title;
    private String abstractText;
    private int citedByCount;
    private String language;
    private String citedByApiUrl;
    private int publicationYear;
    private String publicationDate;
    private String type;
    private String grants;
    private Map<String, Float> keywords;
}
