package com.example.scholar.controller;

import com.example.scholar.dao.UserMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping(value = "/user/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private UserMapper userMapper;

    @PostMapping(value = "/approveApplication")
    @ApiOperation("管理员批准申请接口")
    public R approveApplication(@RequestParam int applicationId,
                                @RequestParam String reason)
    {
        try {
            HashMap<String, Object> resultMap = adminService.approveApplication(applicationId, reason);
            if("申请处理成功".equals(resultMap.get("msg")))
            {
                return R.ok("申请处理成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @PostMapping(value = "/rejectApplication")
    @ApiOperation("管理员拒绝申请接口")
    public R rejectApplication(@RequestParam int applicationId,
                               @RequestParam String reason
                               )
    {
        try {
            HashMap<String, Object> resultMap = adminService.rejectApplication(applicationId, reason);
            if("申请处理成功".equals(resultMap.get("msg")))
            {
                return R.ok("申请处理成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

}
