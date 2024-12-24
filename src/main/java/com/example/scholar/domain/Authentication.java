package com.example.scholar.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Authentication {
    private int id;
    private int userid; // 用户的唯一标志符
    private String nameReal;
    private String workplace;
    private String field;
    private String mail;
    private String authorId;
    private String authorName;
}
