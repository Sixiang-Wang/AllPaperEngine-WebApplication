package com.example.scholar.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllComplaintDto {
    private int id;
    private int userId;
    private String workplace;
    private String mail;
    private String workId;
    private String reason;
    private String addition;
    private int state;
    private String username;
    private String nameReal;
    private String title;
    private List<String> authList;
}
