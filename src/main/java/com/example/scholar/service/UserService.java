package com.example.scholar.service;

import com.example.scholar.domain.User;
import com.example.scholar.domain.myenum.AcademicFieldType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public interface UserService {
    HashMap<String,Object> login(String account, String password);
    HashMap<String, Object> register(String account, String password, String name, String mail, String phone, String company, AcademicFieldType academicField, String profession);
    HashMap<String, Object> updateUserInfo(int userId, String name, String mail, String phone, String company, AcademicFieldType academicField, String profession);
    HashMap<String, Object> changePassword(int userId, String oldPassword, String newPassword);
    HashMap<String, Object> logout(int userId);
}
