package com.example.scholar.dao;

import com.example.scholar.domain.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Date;
import java.util.List;

public interface CommentMapper {
    @Select("select * from user_comment where work_id = #{workId} order by date desc")
    List<Comment> selectCommentsById(String workId);
    @Insert("insert into user_comment (user_id, work_id, date, comment_index) VALUES (#{userId}, #{workId}, #{date}, #{commentIndex})")
    void insertComments(int userId, String workId, Date date, String commentIndex);
    @Delete("delete from user_comment where user_id = #{userId} and work_id = #{workId} and comment_index = #{commentIndex}")
    void deleteUserComment(int userId, String workId, String commentIndex);
    @Select("select count(*) from user_comment_likes where user_id = #{userId} and comment_id = #{commentId}")
    int ifUserLiked(int userId, int commentId);
    @Insert("insert into user_comment_likes (user_id, comment_id) values (#{userId}, #{commentId})")
    void likeComment(int userId, int commentId);
    @Delete("delete from user_comment_likes where user_id = #{userId} and comment_id = #{commentId}")
    void deleteCommentLike(int userId, int commentId);
}
