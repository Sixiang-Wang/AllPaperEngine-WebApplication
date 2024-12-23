package com.example.scholar.dto;

import lombok.Data;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class WorkResultDto {
    private String id;
    private String title;
    private String paperInformation;
    private String abstractText;
    private int cited;
    private String grants;
    private Map<String, Float> keywords;
    private List<String> keywordsList;
    private String publicationDate;
}
