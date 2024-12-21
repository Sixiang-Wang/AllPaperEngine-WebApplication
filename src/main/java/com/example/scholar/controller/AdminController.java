package com.example.scholar.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.scholar.dao.UserMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.AdminService;
import com.example.scholar.util.MailUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping(value = "/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private MailUtil mailUtil;

    /**
     * 判断是否登录成功
     */
    @GetMapping(value = "/login")
    public Object login(@RequestParam("admin")String admin,
                        @RequestParam("password")String password){
        JSONObject jsonObject = new JSONObject();

        int flag = adminService.verifyPassword(admin,password);
        jsonObject.put("code",flag);

        if(flag==-2){
            jsonObject.put("msg","用户名不存在");
            return jsonObject;
        }else if(flag==-1){
            jsonObject.put("msg","密码错误");
            return jsonObject;
        }


        jsonObject.put("msg","登录成功");
        return jsonObject;
    }



    @GetMapping(value = "/register")
    public Object register(@RequestParam("admin")String admin,
                           @RequestParam("password")String password) {
        JSONObject jsonObject = new JSONObject();


        int flag = adminService.putAdmin(admin,password);
        jsonObject.put("code",flag);

        if(flag==-2){
            jsonObject.put("msg","用户名已注册");
            return jsonObject;
        }else if(flag>0){
            mailUtil.sendAdminMail(admin);
            jsonObject.put("msg","注册成功");
            return jsonObject;
        }else {
            jsonObject.put("msg","注册失败,无法连接到服务器");
            return jsonObject;
        }
    }



    @GetMapping(value = "/setPassword")
    public Object setPassword(@RequestParam("admin")String admin,
                              @RequestParam("password")String password) {
        JSONObject jsonObject = new JSONObject();

        int flag = adminService.setPassword(admin,password);
        jsonObject.put("code",flag);

        if(flag==-2){
            jsonObject.put("msg","用户名不存在");
            return jsonObject;
        }else if(flag>0){
            jsonObject.put("msg","修改密码成功");
            return jsonObject;
        }else {
            jsonObject.put("msg","修改失败,无法连接到服务器");
            return jsonObject;
        }
    }

    @GetMapping(value = "/agree")
    public Object agree(@RequestParam("admin")String admin,
                              @RequestParam("code")String code) {
        JSONObject jsonObject = new JSONObject();

        int flag = adminService.agree(admin,code);
        jsonObject.put("code",flag);

        if(flag==-2){
            jsonObject.put("msg","用户不存在");
            return jsonObject;
        }else if(flag>0){
            jsonObject.put("msg","已通过");
            return jsonObject;
        }else if(flag==-114514){
            jsonObject.put("msg","已检测到你在试图攻击服务器!");
            return jsonObject;
        }else {
            jsonObject.put("msg","通过失败,无法连接到服务器");
            return jsonObject;
        }
    }






// 暂时不需要
//    @PostMapping(value = "/approveApplication")
//    @ApiOperation("管理员批准申请接口")
//    public R approveApplication(@RequestParam int applicationId,
//                                @RequestParam String reason)
//    {
//        try {
//            HashMap<String, Object> resultMap = adminService.approveApplication(applicationId, reason);
//            if("申请处理成功".equals(resultMap.get("msg")))
//            {
//                return R.ok("申请处理成功");
//            } else {
//                return R.error((String) resultMap.get("msg"));  // 返回错误信息
//            }
//        }catch (Exception e){
//            return R.error(e.toString());
//        }
//    }
//    @PostMapping(value = "/rejectApplication")
//    @ApiOperation("管理员拒绝申请接口")
//    public R rejectApplication(@RequestParam int applicationId,
//                               @RequestParam String reason
//                               )
//    {
//        try {
//            HashMap<String, Object> resultMap = adminService.rejectApplication(applicationId, reason);
//            if("申请处理成功".equals(resultMap.get("msg")))
//            {
//                return R.ok("申请处理成功");
//            } else {
//                return R.error((String) resultMap.get("msg"));  // 返回错误信息
//            }
//        }catch (Exception e){
//            return R.error(e.toString());
//        }
//    }
//


}
