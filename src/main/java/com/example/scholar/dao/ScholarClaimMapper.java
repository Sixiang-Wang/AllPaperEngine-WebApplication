package com.example.scholar.dao;

import com.example.scholar.domain.UserClaimedWork;
import com.example.scholar.domain.openalex.Work;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScholarClaimMapper {
    @Select("select * from user_claimed_work where scholar_id = #{id}")
    List<UserClaimedWork> selectWorksById(int id);
    @Select("insert into user_claimed_work (scholar_id, work_id) VALUES (#{scholarId},#{workId})")
    void claimWork(int scholarId, String workId);
    @Delete("delete from user_claimed_work where scholar_id = #{scholarId} and work_id = #{workId}")
    void deleteClaimedWorks(int scholarId, String workId);
    @Select("select count(*) from user where userid = #{id} and role = 2")
    int checkIfScholar(int id);
    @Select("select count(*) from user_claimed_work where work_id = #{workId}")
    int checkIfClaimed(String workId);
    @Select("select count(*) from openalex_works where id = #{workId}")
    int checkIfWorkExist(String workId);
}
