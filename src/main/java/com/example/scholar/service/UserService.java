package com.example.scholar.service;

import com.example.scholar.domain.User;
import com.example.scholar.domain.myenum.AcademicFieldType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Component
public interface UserService {
    HashMap<String, Object> login(String mail, String password);
    HashMap<String, Object> register(String name, String password, String mail);
    //HashMap<String, Object> updateUserInfo(int userId, String name, String mail, String phone, String company, AcademicFieldType academicField, String profession);
    HashMap<String, Object> updateUserName(int userId, String username);
    HashMap<String, Object> updateUserAvatar(int userId, String avatar);
    HashMap<String, Object> updateUserBirthTime(int userId, LocalDateTime birthTime);
    HashMap<String, Object> updateUserCompany(int userId, String company);
    HashMap<String, Object> updateUserAcademicField(int userId, AcademicFieldType academicField);
    HashMap<String, Object> updateUserProfession(int userId, String profession);
    HashMap<String, Object> updateUserPhone(int userId, String phone);
    HashMap<String, Object> changePassword(int userId, String oldPassword, String newPassword);

    // 在某个收藏夹添加收藏
    HashMap<String, Object> addUserFavorite(int userId, int publicationId, LocalDateTime timestamp, String folder);

    // 在某个收藏夹取消收藏
    HashMap<String, Object> deleteUserFavorite(int userId, int publicationId, String folder);

    // 查看某个收藏夹所有收藏
    List<HashMap<String, Object>> viewAllFavorites(int userId, String folder);

    // 添加浏览历史
    HashMap<String, Object> addHistory(int userId, int publicationId, LocalDateTime timestamp);

    // 退出登录
    HashMap<String, Object> logout(int userId);

    // 查看所有历史记录
    List<HashMap<String, Object>> viewAllHistory(int userId);

    // 删除某条历史记录
    HashMap<String, Object> deleteHistory(int userId, int publicationId);

    // 清除所有历史记录
    HashMap<String, Object> clearAllHistory(int userId);

    // 创建收藏夹
    HashMap<String, Object> createFavoriteFolder(int userId, String folder);

    // 查看所有收藏夹
    List<HashMap<String, Object>> viewAllFolders(int userId);
}
