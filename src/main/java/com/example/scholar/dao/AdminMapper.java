package com.example.scholar.dao;

import com.example.scholar.domain.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AdminMapper {

    @Select("select count(*) from admin where admin = #{admin} and password = #{password}")
    int verifyPassword(String admin,String password);

    @Select("select * from admin where admin = #{admin}")
    Admin getAdmin(String admin);
    @Insert("insert into admin (admin,password) VALUES (#{admin},#{password})")
    int putAdmin(String admin,String password);

    @Update("update admin set password = #{password} where admin = #{admin}")
    int setPassword(String admin, String password);

    @Select("select count(*) from admin where admin = #{admin}")
    int haveAdmin(String admin);

    @Update("update admin set available = 1 where admin = #{admin}")
    int available(String admin);
}
