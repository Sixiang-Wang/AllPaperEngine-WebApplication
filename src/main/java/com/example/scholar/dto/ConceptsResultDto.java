package com.example.scholar.dto;

import lombok.Data;

@Data
public class ConceptsResultDto {
    private String conceptsId;
    private String wikiurl;
    private String displayName;
    private int level;
    private String description;
    private int worksCount;
    private int citedByCount;
    private String imgUrl;
    private String smallImgUrl;
}
