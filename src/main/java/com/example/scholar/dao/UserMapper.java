package com.example.scholar.dao;

import com.example.scholar.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where userid = #{userId}")
    User selectUserById(int userId);
    @Select("select * from user where account = #{account}")
    User selectUserByAccount(String account);
}
