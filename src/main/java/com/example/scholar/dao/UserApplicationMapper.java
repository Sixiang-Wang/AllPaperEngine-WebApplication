package com.example.scholar.dao;

import com.example.scholar.domain.UserApplication;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserApplicationMapper {
    @Insert("INSERT INTO user_application (user_id, work_id, real_name, application_date, status, mail, message, reason) " +
            "VALUES (#{userId}, #{work_id}, #{realName}, #{company}, #{applicationDate}, #{status}, #{mail}, #{message}, #{reason})")
    void addScholar(UserApplication userApplication);

    @Update("update user_application SET status = #{status}, reason = #{reason} WHERE userid = #{userid}")
    int updateStatus(UserApplication userApplication);

    @Select("select * from user_application where user_id = #{userId}")
    UserApplication getUserApplication(int userId);

    @Select("select * from user_application")
    List<UserApplication> getUserApplications();

}
