package com.example.scholar.controller;
import com.example.scholar.domain.constant.R;
import com.example.scholar.dto.LoginDto;
import com.example.scholar.service.UserService;
import com.example.scholar.service.UserTokenService;
import com.example.scholar.service.impl.FileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;


@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value ="/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private FileService fileService;
    @PostMapping(value = "/login")
    @ApiOperation("登录接口")
    public R login(@ApiParam(value="登录表单") @RequestBody LoginDto loginDto){
        try {
            HashMap<String, Object> res = userService.login(loginDto.getAccount(), loginDto.getPassword());
            if("no such user".equals(res.get("msg"))|| "wrong password".equals(res.get("msg"))){
                return R.ok((String) res.get("msg"));
            }else {
                return R.ok("login success").put("token", res.get("token")).put("username",res.get("username"));
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }


    @ApiOperation("头像接口")
    @RequestMapping(value = "/updateUserAvatar", method = RequestMethod.GET)
    public R updateUserAvatar(@RequestParam("file") MultipartFile profilePictureFile, @RequestParam("userid") int userid){
        if (profilePictureFile.isEmpty()) {
            return R.error();
        }
        String filePath = fileService.updateFile(profilePictureFile);
        userService.updateUserAvatar(userid,filePath);
        return null;
    }
}
