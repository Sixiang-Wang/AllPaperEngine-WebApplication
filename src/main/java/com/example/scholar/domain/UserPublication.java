package com.example.scholar.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UserPublication {
    private int userid;
    private String publicationid;
}
