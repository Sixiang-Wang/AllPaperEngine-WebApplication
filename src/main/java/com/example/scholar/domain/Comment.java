package com.example.scholar.domain;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import java.sql.Date;
@Component
@Data
public class Comment {
    private int id;
    private int userId;
    private String workId;
    @Transient
    private String userName;
    private String commentIndex;
    private Date date;
    private int likes;
}


