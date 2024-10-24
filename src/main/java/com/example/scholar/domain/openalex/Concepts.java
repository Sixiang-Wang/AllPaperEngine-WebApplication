package com.example.scholar.domain.openalex;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Concepts {
    private String id;
    private String wikidata;
    private String displayName;
    private int level;
    private String description;
    private int worksCount;
    private int citedbyCount;
    private String imageUrl;
    private String imageThumbnailUrl;
    private String updatedDate;
}
