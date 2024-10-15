package com.example.scholar.domain.openalex;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AuthorShips {
    private String work_id;
    private String author_position;
    private String author_id;
    private String institution_id;
    private String raw_affiliation_string;
}
