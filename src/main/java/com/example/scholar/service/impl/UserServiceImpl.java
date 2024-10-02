package com.example.scholar.service.impl;


import com.example.scholar.dao.UserMapper;
import com.example.scholar.dao.UserTokenMapper;
import com.example.scholar.domain.User;
import com.example.scholar.service.UserService;
import com.example.scholar.util.JwtUtils;
import com.example.scholar.util.Md5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.example.scholar.config.SystemConst.tokenValidTime;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserTokenMapper userTokenMapper;
    @Override
    public String login(String account, String password) {
        User user = userMapper.selectUserByAccount(account);
        if(user == null){
            return "no such user";
        }else{
            if(Md5Utils.verify(password,user.getPassword())){
                //添加token
                String jwtToken = JwtUtils.createJWT(String.valueOf(user.getUserid()),user.getAccount(),tokenValidTime);
                if(userTokenMapper.ifUserToken(user.getUserid())==1){
                    userTokenMapper.updateUserToken(jwtToken, user.getUserid());
                }else{
                    userTokenMapper.insertUserToken(jwtToken,user.getUserid());
                }
                return jwtToken;
            }else{
                return "wrong password";
            }
        }
    }
}
