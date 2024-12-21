package com.example.scholar.domain;

import lombok.Data;
import org.springframework.stereotype.Component;



/**
 * Admin
 */
@Component
@Data
public class Admin{

    private String admin;
    private String password;
    private int available;
}
