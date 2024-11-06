package com.example.scholar.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

import java.time.LocalDateTime;

@Component
@Data
public class UserFavorite {
    private int userid;
    private int publicationid;
    private LocalDateTime timestamp;
}
