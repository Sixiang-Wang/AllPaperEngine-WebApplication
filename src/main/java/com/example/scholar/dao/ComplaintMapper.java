package com.example.scholar.dao;

import com.example.scholar.domain.Complaint;
import com.example.scholar.domain.UserClaimedWork;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ComplaintMapper {
    @Select("select * from complaint where state = 0")
    List<Complaint> getAllComplaints();

    @Insert("insert into complaint (user_id, name_real, workplace, mail, work_id, reason, addition) values " +
            "(#{userId}, #{nameReal}, #{workplace}, #{mail}, #{workId}, #{reason}, #{addition})")
    void insertComplaint(int userId, String nameReal, String workplace, String mail, String workId,String reason,String addition);

    @Delete("delete from complaint where user_id = #{userId} and work_id = #{workId}")
    void deleteComplaint(int userId, String workId);

    @Delete("delete from user_claim where user_id = #{userId} and work_id = #{workId}")
    void deleteClaimedWork(int userId, String workId);
    @Update("update complaint set state = 1 where id = #{id}")
    void accessComplaint(int id);
    @Update("update complaint set state = -1 where id = #{id}")
    void denyComplaint(int id);
    @Select("select * from complaint where id = #{id}")
    Complaint selectComplaintById(int id);
    @Select("select * from user_claim where work_id = #{id}")
    List<UserClaimedWork> selectUserClaimById(String id);
}
