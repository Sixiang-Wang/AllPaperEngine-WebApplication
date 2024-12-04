package com.example.scholar.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.scholar.dao.UserAppealMapper;
import com.example.scholar.dao.UserMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.UserAppeal;
import com.example.scholar.domain.myenum.AppealReasonType;
import com.example.scholar.service.UserAppealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;

@Service("UserAppeal")
public class UserAppealServiceImpl implements UserAppealService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserAppealMapper userAppealMapper;

    @Override
    public HashMap<String, Object> appealWork(int userId, int workId, String name, String company, AppealReasonType appealReasonType, String mail, String reason) {
        HashMap<String, Object> resultMap = new HashMap<>();
        User existingUser = userMapper.selectUserById(userId);
        if (existingUser == null)
        {
            resultMap.put("msg", "用户不存在");
            return resultMap;
        }
        else{
            if(name == null || name.isEmpty() || company == null || company.isEmpty() || mail == null || mail.isEmpty()){
                resultMap.put("msg", "信息填写不完整");
                return resultMap;
            }
            UserAppeal userAppeal = new UserAppeal();
            userAppeal.setUserId(userId);
            userAppeal.setWorkId(workId);
            userAppeal.setRealName(name);
            userAppeal.setCompany(company);
            userAppeal.setAppealReasonType(appealReasonType);
            userAppeal.setAppealDate(new Timestamp(System.currentTimeMillis()));
            userAppeal.setReason(reason);
            userAppeal.setMail(mail);
            userAppealMapper.addAppeal(userAppeal);
            resultMap.put("msg", "申诉已提交，请等待管理员审核");
            return resultMap;
        }
    }
}
