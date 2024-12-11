package com.example.scholar.domain.openalex;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Data
public class Author {
    private String id;
    private String orcid;
    private String displayName;
    private String displayNameAlternatives;//display_alternatives对应的各种名字
    private int worksCount;
    private int citedByCount;
    private String lastKnownInstitution;
    private String worksApiUrl;
    private String updatedDate;


    //需要一列:是否初始化了 默认是false
    private int firstPublishCount;
    private int highQualityWorkCount;
    private int Hnumber;
    private boolean IsInitialized;

}
