package com.example.scholar.domain.openalex;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AuthorShips {
    private String workId;
    private String authorPosition;
    private String authorId;
    private String rawAffiliationString;
    private String institutions;
}
