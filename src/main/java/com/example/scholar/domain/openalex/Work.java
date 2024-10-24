package com.example.scholar.domain.openalex;

import lombok.Data;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.beans.Transient;

@Component
@Data
public class Work {
    private String id;
    private String doi;
    private String title;
    private String displayName;
    private int publicationYear;
    private String publicationDate;
    private String type;
    private int citedByCount;
    private Boolean isRetracted;
    private Boolean isParatext;
    private String citedByApiUrl;
    private String abstractInvertedIndex;
    private String language;
    private String abstractText;
    private String grants;
    private String keywords;
}
