package com.example.scholar.controller;

import com.example.scholar.config.annotation.TokenToUser;
import com.example.scholar.domain.Authentication;
import com.example.scholar.domain.User;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.AuthenticationService;
import com.example.scholar.service.MessageService;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/authentication")
public class AuthenticationController {
    @Resource
    private AuthenticationService authenticationService;
    @Resource
    private MessageService messageService;
    @GetMapping(value = "/getAll")
    public R getAllAuthentication(){
        try {
            List<Authentication> authenticationList = authenticationService.allAuthentication();
            if(authenticationList == null){
                return R.error("something went wrong");
            }else{
                return R.ok("success").put("authenticationList",authenticationList);
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value = "/getByUserId")
    public R getAuthenticationById(@RequestParam("userId")int userId){
        try {
            List<Authentication> authenticationList = authenticationService.selectAuthenticationById(userId);
            if(authenticationList == null){
                return R.error("something went wrong");
            }else{
                return R.ok("success").put("authenticationList",authenticationList);
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value="/ifauthenticated")
    public R ifAuthenticated(@TokenToUser User user){
        try {
            List<Authentication> list = authenticationService.selectAuthenticationById(user.getUserid());
            if(list.isEmpty()){
                return R.ok("not authenticated");
            }else{
                return R.ok("authenticated");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/put")
    public R putAuthentication(@RequestParam("userId")int userId,
                               @RequestParam("nameReal")String nameReal,
                               @RequestParam("workplace")String workplace,
                               @RequestParam("field")String field,
                               @RequestParam("mail")String mail,
                               @RequestParam("authorId")String authorId,
                               @RequestParam("authorName")String authorName){
        try {
            int result = authenticationService.putAuthentication(userId,nameReal,workplace,field,mail,authorId,authorName);
            if(result == -1){
                return R.error("something went wrong");
            }else{
                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

                messageService.createMessage(1, userId, "您于"+new DateTime(System.currentTimeMillis()).toString(formatter)+
                        "发起的成为科研人员的申请已成功提交，请耐心等待管理员审核。");
                return R.ok("success");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @GetMapping(value = "/put/token")
    @ApiOperation("添加个人门户token版")
    public R putAuthentication(@TokenToUser User user,
                               @RequestParam("nameReal")String nameReal,
                               @RequestParam("workplace")String workplace,
                               @RequestParam("field")String field,
                               @RequestParam("mail")String mail,
                               @RequestParam("authorId")String authorId,
                               @RequestParam("authorId")String authorName
    ){
        try {
            int result = authenticationService.putAuthentication(user.getUserid(),nameReal,workplace,field,mail,authorId,authorName);
            if(result == -1){
                return R.error("something went wrong");
            }else{
                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

                messageService.createMessage(1, user.getUserid(), "您于"+new DateTime(System.currentTimeMillis()).toString(formatter)+
                        "发起的成为科研人员的申请已成功提交，请耐心等待管理员审核。");
                return R.ok("success");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/delete")
    public R deleteAuthentication(@RequestParam("id")int id){
        try {
            int result = authenticationService.deleteAuthentication(id);
            if(result == -1){
                return R.error("something went wrong");
            }else{

                return R.ok("success");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @GetMapping(value = "/haveAuth")
    public R haveAuthentication(@RequestParam("userId")int userId){
        try {
            List<Authentication> authenticationList = authenticationService.selectAuthenticationById(userId);
            if(!authenticationList.isEmpty()){
                return R.ok("success").put("haveAuth",1);
            }else{
                return R.ok("success").put("haveAuth",0);
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

}
