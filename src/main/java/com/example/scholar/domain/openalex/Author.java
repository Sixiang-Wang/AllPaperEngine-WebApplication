package com.example.scholar.domain.openalex;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Data
public class Author {
    private String id;
    private String orcid;
    private String display_name;
    private String display_name_alternatives;//display_alternatives对应的各种名字
    private int works_count;
    private int cited_by_count;
    private String last_known_institution;
    private String works_api_url;
    private String updated_date;

}
