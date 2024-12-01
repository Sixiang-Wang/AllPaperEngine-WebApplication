package com.example.scholar.dao;


import com.example.scholar.domain.Authentication;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuthenticationMapper {
    @Select("select * from user_authentication where userid = #{userid}")
    List<Authentication> selectAuthenticationById(int userid);


    @Select("select * from user_authentication")
    List<Authentication> allAuthenticationById();

    @Select("insert into user_authentication (userid, name_real,workplace,field,mail) VALUES (#{userId},#{nameReal},#{workplace},#{field},#{mail})")
    void putAuthentication(int userId,String nameReal,String workplace,String field,String mail);

    @Delete("delete from user_authentication where id = #{id}")
    void deleteAuthentication(int id);

}
