package com.example.scholar.domain;

import com.example.scholar.domain.myenum.AcademicFieldType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.beans.Transient;
import java.sql.Date;
import java.sql.Timestamp;

@Component
@Data
public class User {
    private int userid;
    private String name;
    private String account;
    private String password;
    private String avatar;
    private Date birthTime;
    private String mail;
    private String phone;
    private String company;
    private AcademicFieldType academicField;
    private String profession;
}
