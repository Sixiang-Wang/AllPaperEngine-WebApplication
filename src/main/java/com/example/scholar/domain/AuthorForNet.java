package com.example.scholar.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.util.List;

@Component
@Data
public class AuthorForNet {
    private String id;
    @Column(columnDefinition = "json")
    private String displayNameAlterNatives;
    private String name;
    List<AuthorForNet> relatedAuthors;
}
