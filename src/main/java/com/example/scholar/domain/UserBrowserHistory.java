package com.example.scholar.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

import java.time.LocalDateTime;

@Component
@Data
public class UserBrowserHistory {
    private int userid;
    private String publicationid;
    private LocalDateTime timestamp;
}
