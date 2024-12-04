package com.example.scholar.domain;

import com.example.scholar.domain.myenum.AcademicFieldType;
import com.example.scholar.domain.myenum.AppealReasonType;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

//学者申诉记录类
@Component
@Data
public class UserAppeal {
    private int appealId;
    private int userId;
    //todo: 论文信息
    private int workId;

    private String realName;
    private String company;
    // 枚举类 申诉原因
    private AppealReasonType appealReasonType;
    private Timestamp appealDate;
    private int status = 0; // 申诉状态 0:待审核 1:已通过 2:已拒绝
    private String mail;
    //补充说明
    private String reason = "";
}
