package com.example.scholar.domain.openalex;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Data
public class Institutions {
    private String id;
    private String ror;//学术组织标识符
    private String displayName;
    private String countryCode;
    private String type;
    private String homapageUrl;
    private String imageUrl;
    private String imageThumbnailUrl;
    private String displayNameAcronyms;
    private String displayNameAlternatives;
    private int worksCouont;
    private int citedByCount;
    private String worksApiUrl;
    private String updatedDate;
}
