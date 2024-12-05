package com.example.scholar.dto;

import lombok.Data;

import java.sql.Date;
@Data
public class MessageDto {
    private int id;
    private String index;
    private Date date;
    private int isRead;
    private String userName;

    public MessageDto(int id, String index, Date date, int isRead, String userName) {
        this.id = id;
        this.index = index;
        this.date = date;
        this.isRead = isRead;
        this.userName = userName;
    }
}
