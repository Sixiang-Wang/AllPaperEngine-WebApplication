package com.example.scholar.dao;

import com.example.scholar.config.PathConfig;
import com.example.scholar.domain.Message;
import org.apache.ibatis.annotations.*;

import javax.persistence.JoinColumn;
import java.sql.Date;
import java.util.List;

public interface MessageMapper {
    @Select("select * from message where to_id = #{userId} order by update_time desc")
    @Results({
            @Result(property = "fromUser", column = "from_id",
             one = @One(select = PathConfig.pathMapper + "UserMapper.selectUserById")),
            @Result(property = "toUser", column = "to_id",
                    one = @One(select = PathConfig.pathMapper + "UserMapper.selectUserById"))
    })
    List<Message> selectMessagesByUser(int userId);
    @Delete("delete from message where id = #{messageId}")
    void deleteMessageById(int messageId);
    @Select("select * from message where id = #{messageId}")
    Message selectMessageById(int messageId);
    @Insert("insert into message (from_id, to_id, message_index, date) VALUES (#{fromId}, #{toId}, #{messageIndex}, #{date})")
    void createMessage(int fromId, int toId, String messageIndex, Date date);
    @Update("update message set is_read = 1 where id = #{messageId}")
    void readMessage(int messageId);
    @Select("select count(*) from message where to_id = #{userId} and is_read = 0")
    int getCountOfUnreadMessage(int userId);
}
