package com.example.scholar.service;

import com.example.scholar.domain.Authentication;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuthenticationService {
    List<Authentication> selectAuthenticationById(int userid);

    List<Authentication> allAuthentication();

    int putAuthentication(int userId,String nameReal,String workplace,String field,String mail,String authorId);
    int deleteAuthentication(int id);

}
