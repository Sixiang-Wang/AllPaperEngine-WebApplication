package com.example.scholar.service.impl;


import com.example.scholar.dao.UserMapper;
import com.example.scholar.dao.UserTokenMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.myenum.AcademicFieldType;
import com.example.scholar.service.UserService;
import com.example.scholar.util.JwtUtils;
import com.example.scholar.util.Md5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
            if(Md5Utils.verify(password, user.getPassword())){
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
    public HashMap<String, Object> register(String account, String password, String name, String mail, String phone, String company, AcademicFieldType academicField, String profession) {
        // 检查账号是否已存在
        User existingUser = userMapper.selectUserByAccount(account);
        HashMap<String,Object> resultMap = new HashMap<>();
        if (existingUser != null) {
            resultMap.put("msg", "account already exists");
            return resultMap;
        }

        // 密码加密
        String hashedPassword = Md5Utils.agenerate(password);

        // 创建新用户对象并赋值
        User newUser = new User();
        newUser.setAccount(account);
        newUser.setPassword(hashedPassword);  // 存储加密后的密码
        newUser.setName(name);
        newUser.setMail(mail);
        newUser.setPhone(phone);
        newUser.setCompany(company);
        newUser.setAcademicField(academicField);
        newUser.setProfession(profession);
        newUser.setAvatar(""); // 可以设置默认头像
        newUser.setBirthTime(LocalDateTime.now()); // 设置当前时间或根据需求提供输入

        // 插入新用户到数据库
        int result = userMapper.insertUser(newUser);
        if (result > 0) {
            resultMap.put("msg", "注册成功");
            resultMap.put("userid", newUser.getUserid());
        } else {
            resultMap.put("msg", "注册失败");
        }

        return resultMap;
    }

    @Override
    public HashMap<String, Object> updateUserInfo(int userId, String name, String mail, String phone, String company, AcademicFieldType academicField, String profession) {
        // 获取当前用户
        User existingUser = userMapper.selectUserById(userId);
        HashMap<String, Object> resultMap = new HashMap<>();

        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }

        if (name != null && !name.isEmpty()) {
            existingUser.setName(name);
        }
        if (mail != null && !mail.isEmpty()) {
            existingUser.setMail(mail);
        }
        if (phone != null && !phone.isEmpty()) {
            existingUser.setPhone(phone);
        }
        if (company != null && !company.isEmpty()) {
            existingUser.setCompany(company);
        }
        if (academicField != null) {
            existingUser.setAcademicField(academicField);
        }
        if (profession != null && !profession.isEmpty()) {
            existingUser.setProfession(profession);
        }

        // 更新数据库
        int result = userMapper.updateUser(existingUser);
        if (result > 0) {
            resultMap.put("msg", "用户信息更新成功");
        } else {
            resultMap.put("msg", "用户信息更新失败");
        }

        return resultMap;
    }

    @Override
    public HashMap<String, Object> changePassword(int userId, String oldPassword, String newPassword) {
        User existingUser = userMapper.selectUserById(userId);
        HashMap<String, Object> resultMap = new HashMap<>();

        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }
        if(oldPassword != null && !oldPassword.isEmpty() && Md5Utils.verify(oldPassword, existingUser.getPassword())){
            if(newPassword != null && !newPassword.isEmpty())
            {
                existingUser.setPassword(Md5Utils.agenerate(newPassword));
                int result = userMapper.updateUser(existingUser);
                if (result > 0) {
                    resultMap.put("msg", "密码修改成功");
                } else {
                    resultMap.put("msg", "密码修改失败");
                }
            }else {
                resultMap.put("msg", "新密码不能为空");
            }
        }else {
            resultMap.put("msg", "旧密码不正确");
        }

        return resultMap;
    }

    @Override
    public HashMap<String, Object> logout(int userId) {

        User existingUser = userMapper.selectUserById(userId);
        HashMap<String, Object> resultMap = new HashMap<>();
        if (existingUser == null) {
            resultMap.put("msg", "User not found");
            return resultMap;
        }

        userTokenMapper.deleteUserToken(userId);
        resultMap.put("msg", "退出登录成功");

        return resultMap;
    }


}
