package com.example.scholar.dao;

import com.example.scholar.domain.UserClaimedWork;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScholarClaimMapper {
    @Select("select * from user_claim where user_id = #{id} and available = 1")
    List<UserClaimedWork> selectWorksById(int id);
    @Select("select * from user_claim where available = 0")
    List<UserClaimedWork> allClaimUnavailable();

    @Select("insert into user_claim(user_id, work_id) VALUES (#{userId},#{workId})")
    void claimWork(int userId, String workId);
    @Delete("delete from user_claim where id = #{id}")
    void deleteClaimedWorks(int id);
    @Select("select count(*) from user where userid = #{id} and role = 1")
    int checkIfScholar(int id);
    @Select("select count(*) from user_claim where work_id = #{workId} and available = 1")
    int checkIfClaimed(String workId);
    @Select("select count(*) from works where id = #{workId}")
    int checkIfWorkExist(String workId);
    @Update("update user_claim SET available = 1 WHERE id = #{id}")
    int ableClaim(int id);
    @Update("update user_claim SET available = 0 WHERE id = #{id}")
    int disableClaim(int id);

}
