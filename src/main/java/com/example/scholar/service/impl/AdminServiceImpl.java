package com.example.scholar.service.impl;

import com.example.scholar.dao.UserApplicationMapper;
import com.example.scholar.dao.UserMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.UserApplication;
import com.example.scholar.service.AdminService;
import com.example.scholar.service.UserApplicationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service("adminService")
public class AdminServiceImpl implements AdminService {
    @Resource
    private UserApplicationMapper userApplicationMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public HashMap<String, Object> approveApplication(int applicationId, String reason) {
        HashMap<String, Object> resultMap = new HashMap<>();
        UserApplication application = userApplicationMapper.getUserApplication(applicationId);
        if(application == null || application.getStatus() != 0) {
            resultMap.put("msg", "该申请已处理");
            return resultMap;
        }
        User user = userMapper.selectUserById(application.getUserId());
        user.setRole(2);
        userMapper.updateUser(user);
        application.setStatus(1);
        application.setReason(reason);
        userApplicationMapper.updateStatus(application);
        resultMap.put("msg","申请处理成功");
        return resultMap;
    }

    @Override
    public HashMap<String, Object> rejectApplication(int applicationId, String reason) {
        HashMap<String, Object> resultMap = new HashMap<>();
        UserApplication application = userApplicationMapper.getUserApplication(applicationId);
        if(application == null || application.getStatus() != 0) {
            resultMap.put("msg", "该申请已处理");
            return resultMap;
        }
        application.setStatus(2);
        application.setReason(reason);
        userApplicationMapper.updateStatus(application);
        resultMap.put("msg","申请处理成功");
        return resultMap;
    }

    @Override
    public List<UserApplication> getApplications() {
        List<UserApplication> applications = userApplicationMapper.getUserApplications();
        return applications;
    }
}
