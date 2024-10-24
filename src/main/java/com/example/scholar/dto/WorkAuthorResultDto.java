package com.example.scholar.dto;

import lombok.Data;

import java.util.Map;

@Data
public class WorkAuthorResultDto {
    private String position;
    private AuthorResultDto authorResultDto;
    private Map<String,InstitutionsResultDto> institutions;
}
