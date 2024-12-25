package com.example.scholar.dto;

import com.example.scholar.domain.openalex.WorksConcepts;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class WorkSpecificResultDto {
    private ArrayList<WorkAuthorResultDto> workAuthorResultDtos;
    private String id;
    private String doi;
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
    private List<String> keywordsList;
    private List<WorksConcepts> worksConceptsList;
}
