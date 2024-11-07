package com.example.scholar.service;

import com.example.scholar.domain.User;
import com.example.scholar.domain.myenum.AcademicFieldType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Component
public interface UserService {
    HashMap<String, Object> login(String mail, String password);
    HashMap<String, Object> register(String name, String password, String mail);
    HashMap<String, Object> setUserDetails(int userId, String avatar, LocalDate birthTime, String biography,
                                           String company, AcademicFieldType academicField, String profession,
                                           String phone);
    HashMap<String, Object> updateUserName(int userId, String username);
    HashMap<String, Object> updateUserBirthTime(int userId, LocalDate birthTime);
    HashMap<String, Object> updateUserCompany(int userId, String company);
    HashMap<String, Object> updateUserAcademicField(int userId, AcademicFieldType academicField);
    HashMap<String, Object> updateUserProfession(int userId, String profession);
    HashMap<String, Object> updateUserPhone(int userId, String phone);
    HashMap<String, Object> changePassword(int userId, String oldPassword, String newPassword);
    HashMap<String, Object> logout(int userId);
    Boolean updateUserAvatar(Integer userid,String avatar);
}
