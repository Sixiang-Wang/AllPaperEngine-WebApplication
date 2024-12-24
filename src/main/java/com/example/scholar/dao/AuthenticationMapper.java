package com.example.scholar.dao;


import com.example.scholar.domain.Authentication;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AuthenticationMapper {
    @Select("select * from user_authentication where userid = #{userid}")
    List<Authentication> selectAuthenticationById(int userid);


    @Select("select * from user_authentication")
    List<Authentication> allAuthentication();

    @Select("insert into user_authentication (userid, name_real,workplace,field,mail) VALUES (#{userId},#{nameReal},#{workplace},#{field},#{mail})")
    void putAuthentication(int userId,String nameReal,String workplace,String field,String mail);

    @Delete("delete from user_authentication where id = #{id}")
    void deleteAuthentication(int id);
    @Update("update user_authentication set author_id = #{id} where userid = #{userId}")
    void updateRelateUserAuthor(int userId, String id);

    @Update("update user_authentication set author_name = #{name} where userid = #{userId}")
    void updateRelateUserAuthorName(int userId, String name);

}
