package com.example.scholar.dto;

import lombok.Data;

@Data
public class ComplaintDto {
    private int userId;
    private String nameReal;
    private String workplace;
    private String mail;
    private String workId;
    private String reason;
    private String addition;
}
