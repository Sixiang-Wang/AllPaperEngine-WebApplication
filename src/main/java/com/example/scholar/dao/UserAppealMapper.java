package com.example.scholar.dao;

import com.example.scholar.domain.UserAppeal;
import org.apache.ibatis.annotations.Insert;

public interface UserAppealMapper {
    @Insert("INSERT INTO user_appeal (user_id, work_id, real_name, appeal_date, status, mail, reason) " +
            "VALUES (#{userId}, #{workId}, #{realName}, #{appealDate}, #{status}, #{mail}, #{reason})")
    void addAppeal(UserAppeal userAppeal);
}
