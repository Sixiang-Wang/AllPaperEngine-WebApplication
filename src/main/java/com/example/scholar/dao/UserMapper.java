package com.example.scholar.dao;

import com.example.scholar.domain.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user")
    List<User> getAll();
    @Select("select count(*) from user")
    int getCount();
    @Select("select * from user where userid = #{userId}")
    User selectUserById(int userId);

    @Select("select count(*) from user where userid = #{userId}")
    int checkIfUser(int userId);

    @Select("select role from user where userid = #{userId}")
    int getUserRole(int userId);
    @Update("update user set author_id = #{id} where userid = #{userId}")
    void updateUserAuthorRelation(String id, int userId);
    @Select("select * from user where mail = #{mail}")
    User selectUserByMail(String mail);

    @Update("update user SET avatar = #{avatar} WHERE userid = #{userid}")
    Integer updateUserAvatar(String userid,String avatar);

    @Select("select avatar from user where userid = #{userid}")
    String getUserAvatar(String userid);
    // 插入新用户
    @Insert("INSERT INTO user (name, password, avatar, birthTime, mail, phone, company, academicField, profession, role, biography) " +
            "VALUES (#{name}, #{password}, #{avatar}, #{birthTime}, #{mail}, #{phone}, #{company}, #{academicField}, #{profession}, #{role}, #{biography})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insertUser(User user);

    @Update({
            "<script>",
            "UPDATE user",
            "<set>",
            "  <if test='name != null and name != \"\"'>name = #{name},</if>",
            "  <if test='password != null and password != \"\"'>password = #{password},</if>",
            "  <if test='mail != null and mail != \"\"'>mail = #{mail},</if>",
            "  <if test='birthTime != null'>birthTime = #{birthTime},</if>",
            "  <if test='phone != null and phone != \"\"'>phone = #{phone},</if>",
            "  <if test='company != null and company != \"\"'>company = #{company},</if>",
            "  <if test='academicField != null'>academicField = #{academicField},</if>",
            "  <if test='profession != null and profession != \"\"'>profession = #{profession},</if>",
            "  <if test='biography != null and biography != \"\"'>biography = #{biography},</if>",
            "</set>",
            "WHERE userid = #{userid}",
            "</script>"
    })
    int updateUser(User user);



    @Update("UPDATE user SET role=#{role} WHERE userid = #{userid}")
    int updateUserRole(User user);
    @Update("UPDATE user SET name_real=#{nameReal} WHERE userid = #{userid}")
    int updateUserNameReal(User user);

    @Update("UPDATE user SET author_id=#{authorId} WHERE userid = #{userid}")
    int updateUserAuthor(User user);

    @Update("UPDATE user SET author_name=#{authorName} WHERE userid = #{userid}")
    int updateUserAuthorName(User user);

    @Update("UPDATE user SET name = #{name} WHERE userid = #{userid}")
    int updateUserName(User user);
    @Update("UPDATE user SET password = #{password} WHERE userid = #{userId}")
    int updatePassword(int userId,String password);

    // 查看某个ID对应的标题
    @Select("SELECT title FROM works WHERE id = #{publicationId}")
    String selectPublicationTitle(String publicationId);

    // 创建标签
    @Insert("INSERT INTO user_favorite_tag (userid, tag) VALUES (#{userId}, #{tag})")
    int createFavoriteTag(int userId, String tag);

    // 检查是否有同名标签
    @Select("SELECT COUNT(*) FROM user_favorite_tag WHERE userid = #{userId} AND tag = #{tag}")
    int checkFavoriteTag(int userId, String tag);

    // 查看用户所有收藏记录
    @Select("<script>" +
            "SELECT uf.publicationid, uf.timestamp, " +
            "       (SELECT GROUP_CONCAT(tag SEPARATOR ',') " +
            "        FROM user_favorite uf2 " +
            "        WHERE uf2.publicationid = uf.publicationid " +
            "          AND uf2.userid = uf.userid " +
            "        GROUP BY uf2.publicationid) AS tags " +
            "FROM user_favorite uf " +
            "WHERE uf.userid = #{userId} " +
            "GROUP BY uf.publicationid, uf.timestamp " +
            "</script>")
    List<HashMap<String, Object>> selectUserFavorite(int userId);

    @Select("select count(*) from user_favorite where userid=#{userId} and publicationid = #{publicationId}")
    int haveFavorite(int userId, String publicationId);

    @Select("select count(DISTINCT userid) from user_favorite where publicationid = #{publicationId}")
    int getWorkFavoriteNum(String publicationId);
    // 在某个标签添加一条收藏记录
    @Insert("INSERT INTO user_favorite (userid, `publicationid`,tag) VALUES (#{userId}, #{publicationId}, #{tag})")
    int addUserFavorite(int userId, String publicationId, String tag);

    // 在某个标签删除一条收藏记录（旧版）
    @Delete("DELETE FROM user_favorite WHERE userid = #{userId} AND publicationid = #{publicationId} AND tag = #{tag}")
    int deleteUserFavoriteOld(int userId, String publicationId, String tag);

    // 删除收藏（新版）
    @Delete("DELETE FROM user_favorite WHERE userid = #{userId} AND publicationid = #{publicationId}")
    int deleteUserFavorite(int userId, String publicationId);

    // 更新收藏记录的时间戳
    @Update("UPDATE user_favorite SET timestamp = #{timestamp} WHERE userid = #{userId} AND publicationid = #{publicationId}")
    void updateUserFavoriteTimestamp(int userId, String publicationId, LocalDateTime timestamp);

    // 查看所有历史记录
    @Select("SELECT * FROM user_browser_history WHERE userid = #{userId} ORDER BY timestamp DESC")
    List<HashMap<String, Object>> selectUserBrowserHistory(int userId);

    // 添加一条历史记录
    @Insert("INSERT INTO user_browser_history (userid, publicationid, timestamp) VALUES (#{userId}, #{publicationId}, #{timestamp})")
    int addUserBrowserHistory(int userId, String publicationId, String timestamp);

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
    int updateUserBrowserHistoryTimestamp(int userId, String publicationId, String timestamp);

    // 查看所有标签
    @Select("SELECT * FROM user_favorite_tag WHERE userid = #{userId}")
    List<HashMap<String, Object>> selectUserFavoriteTag(int userId);

    // 检查某个标签是否非空
    @Select("SELECT COUNT(*) FROM user_favorite WHERE userid = #{userId} AND tag = #{tag}")
    int checkTagNotEmpty(int userId, String tag);

    // 删除某个标签下的所有收藏记录
    @Delete("DELETE FROM user_favorite WHERE userid = #{userId} AND tag = #{tag}")
    int deleteAllFavoritesInTag(int userId, String tag);

    // 删除某个标签
    @Delete("DELETE FROM user_favorite_tag WHERE userid = #{userId} AND tag = #{tag}")
    int deleteFavoriteTag(int userId, String tag);

    // 查询用户每个标签及其标记的文章数量
    @Select("SELECT tag, COUNT(publicationid) AS number " +
            "FROM user_favorite " +
            "WHERE userid = #{userId} " +
            "GROUP BY tag")
    List<HashMap<String, Object>> findTagCountsByUserId(int userId);

    // 查询用户多个标签的收藏交集
    @Select("<script>" +
            "SELECT uf.publicationid, uf.timestamp, " +
            "       (SELECT GROUP_CONCAT(tag SEPARATOR ',') " +
            "        FROM user_favorite uf2 " +
            "        WHERE uf2.publicationid = uf.publicationid " +
            "          AND uf2.userid = uf.userid " +
            "        GROUP BY uf2.publicationid) AS tags " +
            "FROM user_favorite uf " +
            "WHERE uf.userid = #{userId} " +
            "  AND uf.tag IN " +
            "<foreach item='item' index='index' collection='tags' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach> " +
            "GROUP BY uf.publicationid, uf.timestamp " +
            "</script>")
    List<HashMap<String, Object>> findFavoritesWithAnyTags(
            @Param("userId") int userId,
            @Param("tags") List<String> tags
    );

    @Delete("DELETE FROM user WHERE userid = #{userId}")
    int deleteUser(int userId);

    @Select("select * from user where author_name like CONCAT('%', #{name}, '%') and if_show = true")
    List<User> selectScholarsByAuthorName(String name);

    @Select("select display_name from institutions where id = #{id} limit 1")
    String getNameByInstitutionId(String id);
    @Select("select distinct institution_id from works_authorships where author_id = #{id} limit 1")
    String getInstitutionIdByAuthorId(String id);

}
