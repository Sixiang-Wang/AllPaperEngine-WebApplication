package com.example.scholar.dao;

import com.example.scholar.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where userid = #{userId}")
    User selectUserById(int userId);

    @Select("select count(*) from user where userid = #{userId}")
    int checkIfUser(int userId);

    @Select("select role from user where userid = #{userId}")
    int getUserRole(int userId);

    @Select("select * from user where mail = #{mail}")
    User selectUserByMail(String mail);

    @Update("update user SET avatar = #{avatar} WHERE userid = #{userid}")
    Integer updateUserAvatar(String userid,String avatar);

    @Update("update user SET role = #{role} WHERE userid = #{userid}")
    int updateUserRole(int userid,int role);

    // 插入新用户
    @Insert("INSERT INTO user (name, password, avatar, birthTime, mail, phone, company, academicField, profession, role, biography) " +
            "VALUES (#{name}, #{password}, #{avatar}, #{birthTime}, #{mail}, #{phone}, #{company}, #{academicField}, #{profession}, #{role}, #{biography})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insertUser(User user);

    @Update("UPDATE user SET name = #{name}, mail = #{mail}, birthTime = #{birthTime}, phone = #{phone}, company = #{company}, academicField = #{academicField}, profession = #{profession}, biography = #{biography} WHERE userid = #{userid}")
    int updateUser(User user);

    @Update("UPDATE user SET name = #{name} WHERE userid = #{userid}")
    int updateUserName(User user);
}
