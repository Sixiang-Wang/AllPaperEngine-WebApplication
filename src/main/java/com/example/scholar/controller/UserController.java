package com.example.scholar.controller;
import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.myenum.AcademicFieldType;
import com.example.scholar.dto.LoginDto;
import com.example.scholar.dto.RegistDto;
import com.example.scholar.service.UserService;
import com.example.scholar.service.UserTokenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;
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
    @PostMapping(value = "/login")
    @ApiOperation("登录接口")
    public R login(@ApiParam(value="登录表单") @RequestBody LoginDto loginDto){
        try {
            HashMap<String, Object> res = userService.login(loginDto.getMail(), loginDto.getPassword());
            if("no such user".equals(res.get("msg"))|| "wrong password".equals(res.get("msg"))){
                return R.ok((String) res.get("msg"));
            }else {
                return R.ok("login success").put("token", res.get("token")).put("username",res.get("username"));
            }
        }catch (Exception e){
            return R.error(e.toString());
        }
    }
    @PostMapping(value = "/register")
    @ApiOperation("注册接口")
    public R register(@ApiParam(value="登录表单") @RequestBody RegistDto registDto) {
        String name = registDto.getName();
        String password = registDto.getPassword();
        String mail = registDto.getMail();
        try {
            HashMap<String, Object> res = userService.register(name, password, mail);
            if ("account already exists".equals(res.get("msg"))) {
                return R.error("account already exists");
            } else if ("注册成功".equals(res.get("msg"))) {
                return R.ok("register success").put("userid", res.get("userid"));
            } else {
                return R.error("register failed");
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }
    @PostMapping(value = "/updateUserName")
    @ApiOperation("修改用户名接口")
    public R updateUsername(@RequestParam int userId,
                            @RequestParam(required = false) String name)
    {
        try {
            HashMap<String, Object> resultMap = userService.updateUserName(userId, name);
            if ("用户名更新成功".equals(resultMap.get("msg"))) {
                return R.ok("用户名更新成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @PostMapping(value = "/updateUserAvatar")
    @ApiOperation("修改用户头像接口")
    public R updateUseravatar(@RequestParam int userId,
                              @RequestParam(required = false) String avatar)
    {
        try {
            HashMap<String, Object> resultMap = userService.updateUserAvatar(userId, avatar);
            if ("用户头像更新成功".equals(resultMap.get("msg"))) {
                return R.ok("用户头像更新成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @PostMapping(value = "/updateUserBirthTime")
    @ApiOperation("修改用户生日接口")
    public R updateUserbirthTime(@RequestParam int userId,
                                 @RequestParam(required = false) LocalDateTime birthTime)
    {
        try {
            HashMap<String, Object> resultMap = userService.updateUserBirthTime(userId, birthTime);
            if ("用户生日更新成功".equals(resultMap.get("msg"))) {
                return R.ok("用户生日更新成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @PostMapping(value = "/updateUserCompany")
    @ApiOperation("修改在职单位接口")
    public R updateUsercompany(@RequestParam int userId,
                               @RequestParam(required = false) String company)
    {
        try {
            HashMap<String, Object> resultMap = userService.updateUserCompany(userId, company);
            if ("在职单位更新成功".equals(resultMap.get("msg"))) {
                return R.ok("在职单位更新成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @PostMapping(value = "/updateUserAcademicField")
    @ApiOperation("修改学术领域接口")
    public R updateUserAcademicField(@RequestParam int userId,
                                     @RequestParam(required = false) AcademicFieldType academicField)
    {
        try {
            HashMap<String, Object> resultMap = userService.updateUserAcademicField(userId, academicField);
            if ("学术领域更新成功".equals(resultMap.get("msg"))) {
                return R.ok("学术领域更新成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @PostMapping(value = "/updateUserProfession")
    @ApiOperation("修改用户职业接口")
    public R updateUserProfession(@RequestParam int userId,
                                  @RequestParam(required = false) String profession)
    {
        try {
            HashMap<String, Object> resultMap = userService.updateUserProfession(userId, profession);
            if ("用户职业更新成功".equals(resultMap.get("msg"))) {
                return R.ok("用户职业更新成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @PostMapping(value = "/updateUserPhone")
    @ApiOperation("修改用户电话接口")
    public R updateUserPhone(@RequestParam int userId,
                             @RequestParam(required = false) String phone)
    {
        try {
            HashMap<String, Object> resultMap = userService.updateUserPhone(userId, phone);
            if ("用户电话更新成功".equals(resultMap.get("msg"))) {
                return R.ok("用户电话更新成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @PostMapping(value = "/changePassword")
    @ApiOperation("修改密码接口")
    public R changePassword(
            @RequestParam int userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            HashMap<String, Object> res = userService.changePassword(userId, oldPassword, newPassword);
            if ("密码修改成功".equals(res.get("msg"))) {
                return R.ok("Password changed successfully");
            } else {
                return R.error((String) res.get("msg"));
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

    @PostMapping(value = "/logout")
    @ApiOperation("退出登录接口")
    public R logout(@RequestParam int userId) {
        try {
            HashMap<String, Object> resultMap = userService.logout(userId);
            return R.ok((String) resultMap.get("msg"));
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

}
