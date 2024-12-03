package com.example.scholar.dao;

import com.example.scholar.domain.UserApplication;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserApplicationMapper {
    @Insert("INSERT INTO user_application (user_id, application_date, status, mail, message, reason) " +
            "VALUES (#{userId}, #{applicationDate}, #{status}, #{mail}, #{message}, #{reason})")
    void addScholar(UserApplication userApplication);

    @Update("update user_application SET status = #{status}, reason = #{reason} WHERE userid = #{userid}")
    int updateStatus(UserApplication userApplication);

    @Select("select * from user_application where userid = #{userId}")
    UserApplication getUserApplication(int userid);

    @Select("select * from user_application")
    List<UserApplication> getUserApplications();

}
