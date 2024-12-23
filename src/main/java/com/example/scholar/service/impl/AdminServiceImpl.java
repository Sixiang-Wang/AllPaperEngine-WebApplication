package com.example.scholar.service.impl;

import com.example.scholar.dao.AdminMapper;
import com.example.scholar.domain.Admin;
import com.example.scholar.service.AdminService;
import com.example.scholar.util.MailUtil;
import com.example.scholar.util.Md5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private MailUtil mailUtil;

    @Override
    public int verifyPassword(String admin, String password) {
        if(admin==null|| admin.isEmpty() || adminMapper.haveAdmin(admin)<=0){
            return -2;
        } else {
            String hashedPassword = adminMapper.getAdmin(admin).getPassword();
            if(hashedPassword!=null&&Md5Utils.verify(password, hashedPassword)){
                return 1;
            }else {
                return -1;
            }
        }
    }

    @Override
    public int putAdmin(String admin, String password) {

        if(admin==null|| admin.isEmpty() || adminMapper.haveAdmin(admin)>0 ){
            return -2;
        }
        String hashedPassword = Md5Utils.agenerate(password);

        return adminMapper.putAdmin(admin,hashedPassword);
    }

    @Override
    public int setPassword(String admin, String password) {
        if(admin==null|| admin.isEmpty() || adminMapper.haveAdmin(admin)<=0){
            return -2;
        }


        String hashedPassword = Md5Utils.agenerate(password);
        return adminMapper.setPassword(admin,hashedPassword);
    }

    @Override
    public int agree(String admin, String code) {
        if(admin==null|| admin.isEmpty() || adminMapper.haveAdmin(admin)<=0){
            return -2;
        }

        if(!Md5Utils.verify(admin, code)){
            return -114514;
        }
        return adminMapper.available(admin);
    }

    @Override
    public boolean haveAdmin(String admin) {
        return adminMapper.haveAdmin(admin)>0;
    }





    //下面真用不着:(
//    @Resource
//    private UserApplicationMapper userApplicationMapper;
//    @Resource
//    private UserMapper userMapper;
//    @Override
//    public HashMap<String, Object> approveApplication(int applicationId, String reason) {
//        HashMap<String, Object> resultMap = new HashMap<>();
//        UserApplication application = userApplicationMapper.getUserApplication(applicationId);
//        if(application == null || application.getStatus() != 0) {
//            resultMap.put("msg", "该申请已处理");
//            return resultMap;
//        }
//        User user = userMapper.selectUserById(application.getUserId());
//        user.setRole(2);
//        userMapper.updateUser(user);
//        application.setStatus(1);
//        application.setReason(reason);
//        userApplicationMapper.updateStatus(application);
//        resultMap.put("msg","申请处理成功");
//        return resultMap;
//    }
//
//    @Override
//    public HashMap<String, Object> rejectApplication(int applicationId, String reason) {
//        HashMap<String, Object> resultMap = new HashMap<>();
//        UserApplication application = userApplicationMapper.getUserApplication(applicationId);
//        if(application == null || application.getStatus() != 0) {
//            resultMap.put("msg", "该申请已处理");
//            return resultMap;
//        }
//        application.setStatus(2);
//        application.setReason(reason);
//        userApplicationMapper.updateStatus(application);
//        resultMap.put("msg","申请处理成功");
//        return resultMap;
//    }
//
//    @Override
//    public List<UserApplication> getApplications() {
//        List<UserApplication> applications = userApplicationMapper.getUserApplications();
//        return applications;
//    }
}
