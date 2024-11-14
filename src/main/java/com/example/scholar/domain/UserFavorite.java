package com.example.scholar.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

import java.time.LocalDateTime;

/*
 * userid: 用户id
 * publicationid: 学术成果id
 * timestamp: 收藏时间
 * tag: 标签名称
 */
@Component
@Data
public class UserFavorite {
    private int userid;
    private String publicationid;
    private LocalDateTime timestamp;
    private String tag;
}
