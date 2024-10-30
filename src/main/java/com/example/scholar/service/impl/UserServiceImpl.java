package com.example.scholar.service.impl;


import com.example.scholar.dao.UserMapper;
import com.example.scholar.dao.UserTokenMapper;
import com.example.scholar.domain.User;
import com.example.scholar.service.UserService;
import com.example.scholar.util.JwtUtils;
import com.example.scholar.util.Md5Utils;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

import static com.example.scholar.config.SystemConst.tokenValidTime;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserTokenMapper userTokenMapper;
    @Override
    public HashMap<String, Object> login(String account, String password) {
        User user = userMapper.selectUserByAccount(account);
        HashMap<String,Object> map = new HashMap<>();
        if(user == null){
            map.put("msg", "no such user");
            return map;
        }else{
            if(Md5Utils.verify(password,user.getPassword())){
                //添加token
                String jwtToken = JwtUtils.createJWT(String.valueOf(user.getUserid()),user.getAccount(),tokenValidTime);
                if(userTokenMapper.ifUserToken(user.getUserid())==1){
                    userTokenMapper.updateUserToken(jwtToken, user.getUserid());
                }else{
                    userTokenMapper.insertUserToken(jwtToken,user.getUserid());
                }
                map.put("token",jwtToken);
                map.put("username",user.getName());
                return map;
            }else{
                map.put("msg","wrong password");
                return map;
            }
        }
    }

    @Override
    public Boolean updateUserAvatar(Integer userid, String avatar){
        return userMapper.updateUserAvatar(userid.toString(),avatar)>0;
    }
}
