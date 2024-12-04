package com.example.scholar.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.scholar.controller.MailController;
import com.example.scholar.dao.*;
import com.example.scholar.domain.User;
import com.example.scholar.domain.UserApplication;
import com.example.scholar.domain.myenum.AcademicFieldType;
import com.example.scholar.service.MailService;
import com.example.scholar.service.UserApplicationService;
import com.example.scholar.service.WorkService;
import com.example.scholar.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;

@Service("UserApplication")
public class UserApplicationServiceImpl implements UserApplicationService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserApplicationMapper userApplicationMapper;
    @Resource
    private MailService mailService;

    @Override
    public HashMap<String, Object> certifyScholar(int userId, String name, String company, AcademicFieldType academicField, String mail, String code) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null)
        {
            resultMap.put("msg", "用户不存在");
            return resultMap;
        }
        else{
            int usrRole = existingUser.getRole();
            if(usrRole == 2)
            {
                resultMap.put("msg", "当前用户已经是学者");
                return resultMap;
            }
            else if(usrRole == 1){
                if(name == null || name.isEmpty() || company == null || company.isEmpty()
                    || academicField == null || mail == null || mail.isEmpty())
                {
                    resultMap.put("msg", "信息填写不完整");
                    return resultMap;
                }
                JSONObject verifyResult = mailService.verifyCode(mail, code);
                if (verifyResult.containsKey("error")) {
                    resultMap.put("msg", verifyResult.getString("error"));
                    return resultMap;
                } else if ("验证码校验成功".equals(verifyResult.getString("msg"))) {
                    UserApplication userApplication = new UserApplication();
                    userApplication.setUserId(userId);
                    userApplication.setRealName(name);
                    userApplication.setCompany(company);
                    userApplication.setAcademicFieldType(academicField);
                    userApplication.setMail(mail);
                    userApplication.setApplicationDate(new Timestamp(System.currentTimeMillis()));
                    userApplication.setStatus(0);
                    userApplication.setMessage(code);
                    userApplicationMapper.addScholar(userApplication);
                    resultMap.put("msg", "学者认证申请已提交，请等待管理员审核");
                    return resultMap;
                }
            }
        }
        return resultMap;
    }

}
