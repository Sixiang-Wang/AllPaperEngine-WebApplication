package com.example.scholar.service.impl;

import com.example.scholar.dao.AuthenticationMapper;
import com.example.scholar.domain.Authentication;
import com.example.scholar.service.AuthenticationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
    @Resource
    private AuthenticationMapper authenticationMapper;

    @Override
    public List<Authentication> selectAuthenticationById(int userid) {
        return authenticationMapper.selectAuthenticationById(userid);
    }

    @Override
    public List<Authentication> allAuthentication() {
        return authenticationMapper.allAuthenticationById();
    }

    @Override
    public int putAuthentication(int userId, String nameReal, String workplace, String field, String mail) {
        List<Authentication> authenticationList = authenticationMapper.selectAuthenticationById(userId);
        if(authenticationList.isEmpty()){
            authenticationMapper.putAuthentication(userId,nameReal,workplace,field,mail);
            return 1;
        }else {
            return -1;
        }
    }

    @Override
    public int deleteAuthentication(int id) {
        authenticationMapper.deleteAuthentication(id);
        return 1;
    }
}
