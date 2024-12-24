package com.example.scholar.dto;


import com.example.scholar.domain.openalex.Author;
import com.example.scholar.domain.openalex.Institutions;
import com.example.scholar.service.AuthorService;
import com.example.scholar.util.TopicAnalyzer;
import lombok.Data;

@Data
public class AuthorSpecificResultDto {
    private Author author;
    private Institutions institutions;

    public AuthorSpecificResultDto(Author author, Institutions institutionById) {
        this.author = author;
        this.institutions = institutionById;
    }

    public AuthorSpecificResultDto() {
    }

    //    private TopicAnalyzer.Topic TopicDisplayName;

}
