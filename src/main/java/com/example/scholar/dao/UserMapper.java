package com.example.scholar.dao;

import com.example.scholar.domain.User;
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

    @Update("update user SET avatar = #{avatar} WHERE userid = #{userid}")
    Integer updateUserAvatar(String userid,String avatar);

    // 插入新用户
    @Insert("INSERT INTO user (name, password, avatar, birthTime, mail, phone, company, academicField, profession, role, biography) " +
            "VALUES (#{name}, #{password}, #{avatar}, #{birthTime}, #{mail}, #{phone}, #{company}, #{academicField}, #{profession}, #{role}, #{biography})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insertUser(User user);

    @Update("UPDATE user SET name = #{name}, mail = #{mail}, birthTime = #{birthTime}, phone = #{phone}, company = #{company}, academicField = #{academicField}, profession = #{profession}, biography = #{biography} WHERE userid = #{userid}")
    int updateUser(User user);

    @Update("UPDATE user SET name = #{name} WHERE userid = #{userid}")
    int updateUserName(User user);

    // 查看某个ID对应的标题
    @Select("SELECT title FROM openalex_works WHERE id = #{publicationId}")
    String selectPublicationTitle(String publicationId);

    // 创建收藏夹
    @Insert("INSERT INTO user_favorite_folder (userid, folder) VALUES (#{userId}, #{folder})")
    int createFavoriteFolder(int userId, String folder);

    // 检查是否有同名收藏夹
    @Select("SELECT COUNT(*) FROM user_favorite_folder WHERE userid = #{userId} AND folder = #{folder}")
    int checkFavoriteFolder(int userId, String folder);

    // 查看某个收藏夹所有收藏记录
    @Select("SELECT * FROM user_favorite WHERE userid = #{userId} AND folder = #{folder}")
    List<HashMap<String, Object>> selectUserFavorite(int userId, String folder);

    // 在某个收藏夹添加一条收藏记录
    @Insert("INSERT INTO user_favorite (userid, publicationid, timestamp, folder) VALUES (#{userId}, #{publicationId}, #{timestamp}, #{folder})")
    int addUserFavorite(int userId, String publicationId, LocalDateTime timestamp, String folder);

    // 在某个收藏夹删除一条收藏记录
    @Delete("DELETE FROM user_favorite WHERE userid = #{userId} AND publicationid = #{publicationId} AND folder = #{folder}")
    int deleteUserFavorite(int userId, String publicationId, String folder);

    // 查看所有历史记录
    @Select("SELECT * FROM user_browser_history WHERE userid = #{userId}")
    List<HashMap<String, Object>> selectUserBrowserHistory(int userId);

    // 添加一条历史记录
    @Insert("INSERT INTO user_browser_history (userid, publicationid, timestamp) VALUES (#{userId}, #{publicationId}, #{timestamp})")
    int addUserBrowserHistory(int userId, String publicationId, LocalDateTime timestamp);

    // 删除一条历史记录
    @Delete("DELETE FROM user_browser_history WHERE userid = #{userId} AND publicationid = #{publicationId}")
    int deleteUserBrowserHistory(int userId, String publicationId);

    // 清空所有历史记录
    @Delete("DELETE FROM user_browser_history WHERE userid = #{userId}")
    int clearUserBrowserHistory(int userId);

    // 检查是否有相同的历史记录
    @Select("SELECT COUNT(*) FROM user_browser_history WHERE userid = #{userId} AND publicationid = #{publicationId}")
    int checkUserBrowserHistory(int userId, String publicationId);

    // 修改已有记录的时间戳
    @Update("UPDATE user_browser_history SET timestamp = #{timestamp} WHERE userid = #{userId} AND publicationid = #{publicationId}")
    int updateUserBrowserHistoryTimestamp(int userId, String publicationId, LocalDateTime timestamp);

    // 查看所有收藏夹
    @Select("SELECT * FROM user_favorite_folder WHERE userid = #{userId}")
    List<HashMap<String, Object>> selectUserFavoriteFolder(int userId);

    // 检查某个收藏夹是否非空
    @Select("SELECT COUNT(*) FROM user_favorite WHERE userid = #{userId} AND folder = #{folder}")
    int checkFolderNotEmpty(int userId, String folder);

    // 删除某个收藏夹下的所有收藏记录
    @Delete("DELETE FROM user_favorite WHERE userid = #{userId} AND folder = #{folder}")
    int deleteAllFavoritesInFolder(int userId, String folder);

    // 删除某个收藏夹
    @Delete("DELETE FROM user_favorite_folder WHERE userid = #{userId} AND folder = #{folder}")
    int deleteFavoriteFolder(int userId, String folder);
}
