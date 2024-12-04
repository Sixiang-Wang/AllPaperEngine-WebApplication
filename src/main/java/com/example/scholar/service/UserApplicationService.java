package com.example.scholar.service;

import com.example.scholar.domain.myenum.AcademicFieldType;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public interface UserApplicationService {
    HashMap<String, Object> certifyScholar(int userId, String name, String company, AcademicFieldType academicField, String mail, String code); // 认证成为学者
}
