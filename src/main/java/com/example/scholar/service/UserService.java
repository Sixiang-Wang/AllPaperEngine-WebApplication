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
    List<User> getAll();
    int getCount();
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
    public HashMap<String, Object> updatePassword(int userId, String newPassword);
    // 在某个标签添加收藏
    HashMap<String, Object> addUserFavorite(int userId, String publicationId, LocalDateTime timestamp, List<String> tags);

    // 在某个标签取消收藏
    HashMap<String, Object> deleteUserFavorite(int userId, String publicationId, String tag);

    // 添加浏览历史
    HashMap<String, Object> addHistory(int userId, String publicationId, LocalDateTime timestamp);

    // 退出登录
    HashMap<String, Object> logout(int userId);

    HashMap<String, Object> delete(int userId);
    // 查看所有历史记录
    List<HashMap<String, Object>> viewAllHistory(int userId);

    // 删除某条历史记录
    HashMap<String, Object> deleteHistory(int userId, String publicationId);

    // 清除所有历史记录
    HashMap<String, Object> clearAllHistory(int userId);

    // 创建标签
    HashMap<String, Object> createFavoriteTag(int userId, String tag);

    // 查看所有标签
    List<HashMap<String, Object>> viewAllTags(int userId);

    // 删除标签
    HashMap<String, Object> deleteFavoriteTag(int userId, String tag);

    // 查看某个用户的所有收藏
    List<HashMap<String, Object>> viewAllFavoritesByUser(int userId);

    // 查看多个标签下的所有收藏
    List<HashMap<String, Object>> viewAllFavoritesWithTags(int userId, List<String> tags);

    // 查看所有收藏的所有标签和收藏数量
    List<HashMap<String, Object>> viewAllTagsAndCounts(int userId);

    Boolean updateUserAvatar(Integer userid,String avatar);
}
