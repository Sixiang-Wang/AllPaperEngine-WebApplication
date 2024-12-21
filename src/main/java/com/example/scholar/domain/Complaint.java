package com.example.scholar.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Complaint {
    private int id;
    private int userId;
    private String nameReal;
    private String workplace;
    private String mail;
    private String workId;
    private String reason;
    private String addition;
    private int state;
}
