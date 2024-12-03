package com.example.scholar.domain;

import com.example.scholar.domain.myenum.AcademicFieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

// 学者认证记录类
@Component
@Data
public class UserApplication {
    private int applicationId;
    private int userId;
    private String realName;
    private String company;
    private AcademicFieldType academicFieldType;
    private Timestamp applicationDate;
    private int status = 0; // 申请状态 0:待审核 1:已通过 2:已拒绝
    private String mail;
    private String message = "";
    private String reason = ""; // 审核原因
}
