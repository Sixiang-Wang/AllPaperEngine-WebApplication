package com.example.scholar.dao;

import com.example.scholar.domain.UserToken;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.Date;

public interface UserTokenMapper {
    @Select("select * from usertoken where token = #{token}")
    UserToken selectByToken(String token);
    @Insert("insert into usertoken (userid,token, updatetime, expiretime) VALUES (#{id},#{token},now(),now()+60000)")
    void insertUserToken(String token, int id);
    @Select("select count(*) from usertoken where userid=#{id}")
    int ifUserToken(int id);
    @Update("update usertoken set token = #{token}, updatetime = now(), expiretime = now()+60000 where userid = #{id}")
    void updateUserToken(String token, int id);
}
