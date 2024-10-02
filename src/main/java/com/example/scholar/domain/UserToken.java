package com.example.scholar.domain;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

@Data
@Component
@Slf4j
public class UserToken {
    private int userid;
    private String token;
    private Timestamp updatetime;
    private Timestamp expiretime;
}
