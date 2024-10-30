package com.example.scholar.dao;

import com.example.scholar.domain.User;
import com.example.scholar.domain.myenum.AcademicFieldType;
import org.apache.ibatis.annotations.*;

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


}
