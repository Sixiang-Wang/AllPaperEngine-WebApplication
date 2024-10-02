package com.example.scholar.controller;
import com.example.scholar.domain.R;
import com.example.scholar.dto.LoginDto;
import com.example.scholar.service.UserService;
import com.example.scholar.service.UserTokenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value ="/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserTokenService userTokenService;
    @PostMapping(value = "/login")
    @ApiOperation("登录接口")
    public R login(@ApiParam(value="登录表单") @RequestBody LoginDto loginDto){
        try {
            String res = userService.login(loginDto.getAccount(), loginDto.getPassword());
            if("no such user".equals(res)|| "wrong password".equals(res)){
                return R.ok(res);
            }else {
                return R.ok("login success").put("token", res);
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
}
