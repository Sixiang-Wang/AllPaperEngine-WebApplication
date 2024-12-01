package com.example.scholar.controller;

import com.example.scholar.domain.Authentication;
import com.example.scholar.domain.constant.R;
import com.example.scholar.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/authentication")
public class AuthenticationController {
    @Resource
    private AuthenticationService authenticationService;

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

    @GetMapping(value = "/put")
    public R putAuthentication(@RequestParam("userId")int userId,@RequestParam("nameReal")String nameReal,@RequestParam("workplace")String workplace,@RequestParam("field")String field,@RequestParam("mail")String mail){
        try {
            int result = authenticationService.putAuthentication(userId,nameReal,workplace,field,mail);
            if(result == -1){
                return R.error("something went wrong");
            }else{
                return R.ok("success");
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }

    @GetMapping(value = "/delete")
    public R putAuthentication(@RequestParam("id")int id){
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
