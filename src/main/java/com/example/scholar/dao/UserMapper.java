package com.example.scholar.dao;

import com.example.scholar.domain.User;
import com.example.scholar.domain.myenum.AcademicFieldType;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where userid = #{userId}")
    User selectUserById(int userId);

    @Select("select * from user where mail = #{mail}")
    User selectUserByMail(String mail);

    // 插入新用户
    @Insert("INSERT INTO user (name, password, avatar, birthTime, mail, phone, company, academicField, profession, role, biography) " +
            "VALUES (#{name}, #{password}, #{avatar}, #{birthTime}, #{mail}, #{phone}, #{company}, #{academicField}, #{profession}, #{role}, #{biography})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insertUser(User user);

    @Update("UPDATE user SET name = #{name}, mail = #{mail}, phone = #{phone}, company = #{company}, academicField = #{academicField}, profession = #{profession} WHERE userid = #{userid}")
    int updateUser(User user);

    @Update("UPDATE user SET name = #{name} WHERE userid = #{userid}")
    int updateUserName(User user);

    // 查看所有收藏记录
    @Select("SELECT * FROM user_favorite WHERE userid = #{userId}")
    List<HashMap<String, Object>> selectUserFavorite(int userId);

    // 添加一条收藏记录
    @Insert("INSERT INTO user_favorite (userid, publicationid, timestamp) VALUES (#{userId}, #{publicationId}, #{timestamp})")
    int addUserFavorite(int userId, int publicationId, LocalDateTime timestamp);

    // 删除一条收藏记录
    @Delete("DELETE FROM user_favorite WHERE userid = #{userId} AND publicationid = #{publicationId}")
    int deleteUserFavorite(int userId, int publicationId);

    // 查看所有历史记录
    @Select("SELECT * FROM user_browser_history WHERE userid = #{userId}")
    List<HashMap<String, Object>> selectUserBrowserHistory(int userId);

    // 添加一条历史记录
    @Insert("INSERT INTO user_browser_history (userid, publicationid, timestamp) VALUES (#{userId}, #{publicationId}, #{timestamp})")
    int addUserBrowserHistory(int userId, int publicationId, LocalDateTime timestamp);

    // 删除一条历史记录
    @Delete("DELETE FROM user_browser_history WHERE userid = #{userId} AND publicationid = #{publicationId}")
    int deleteUserBrowserHistory(int userId, int publicationId);

    // 清空所有历史记录
    @Delete("DELETE FROM user_browser_history WHERE userid = #{userId}")
    int clearUserBrowserHistory(int userId);

    // 检查是否有相同的历史记录
    @Select("SELECT COUNT(*) FROM user_browser_history WHERE userid = #{userId} AND publicationid = #{publicationId}")
    int checkUserBrowserHistory(int userId, int publicationId);

    // 修改已有记录的时间戳
    @Update("UPDATE user_browser_history SET timestamp = #{timestamp} WHERE userid = #{userId} AND publicationid = #{publicationId}")
    int updateUserBrowserHistoryTimestamp(int userId, int publicationId, LocalDateTime timestamp);
}
