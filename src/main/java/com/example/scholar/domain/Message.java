package com.example.scholar.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Date;

@Component
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String messageIndex;
    private Date date;
    private int isRead;
    @ManyToOne
    @JoinColumn(name="from_id")
    private User fromUser;
    @ManyToOne
    @JoinColumn(name="to_id")
    private User toUser;
}
