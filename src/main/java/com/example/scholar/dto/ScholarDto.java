package com.example.scholar.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ScholarDto {
    private String authorId;
    private String avatar;
    private String institution;
    private String authorName;
    private String userName;
    private String mail;
}
