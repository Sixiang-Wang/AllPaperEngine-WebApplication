package com.example.scholar.domain;

import com.example.scholar.domain.myenum.AcademicFieldType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.beans.Transient;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@Data
public class User {
    private int userid; // 用户的唯一标志符

    private String name; //用户名
    private String avatar = "https://i.afbcs.cn/indGzF"; //头像
    private LocalDateTime birthTime; //出生年月
    private String biography; //个人简介

    private String company; //在职单位
    private AcademicFieldType academicField; //学术领域
    private String profession; //职业

    private String mail; //用户邮箱（注册时使用）
    private String password;
    private String phone; //手机号

    private int role; //用户角色
}
