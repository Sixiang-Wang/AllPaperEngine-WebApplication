package com.example.scholar.controller;
import com.example.scholar.domain.constant.CheckResult;
import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.myenum.AcademicFieldType;
import com.example.scholar.dto.LoginDto;
import com.example.scholar.dto.RegistDto;
import com.example.scholar.service.UserService;
import com.example.scholar.service.UserTokenService;
import com.example.scholar.service.impl.FileService;
import com.example.scholar.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
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
        @Resource
        private FileService fileService;
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

    @PostMapping(value = "/setUserDetails")
    @ApiOperation("设置用户详细信息接口")
    public R setUserDetails(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String avatar,
            @RequestParam(required = false) LocalDate birthTime,
            @RequestParam(required = false) String biography,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) AcademicFieldType academicField,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) String phone) {
        try {
            // 验证 token 并获取 userId
            CheckResult checkResult = JwtUtils.validateJWT(token);
            if (!checkResult.isSuccess()) {
                return R.error("Token无效或已过期");
            }

            Claims claims = checkResult.getClaims();
            int userId = Integer.parseInt(claims.getId());  // 从 token 中获取用户ID

            // 使用 userService 更新用户详细信息
            HashMap<String, Object> resultMap = userService.setUserDetails(
                    userId, avatar, birthTime, biography, company, academicField, profession, phone);

            if ("详细信息更新成功".equals(resultMap.get("msg"))) {
                return R.ok("详细信息更新成功");
            } else {
                return R.error((String) resultMap.get("msg"));  // 返回错误信息
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }

        @PostMapping(value = "/updateUserName")
        @ApiOperation("修改用户名接口")
        public R updateUsername(
                @RequestHeader("Authorization") String token,  // 从请求头中获取JWT
                @RequestParam(required = false) String name)
        {
            try {
                // 解析 token 获取 userId
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }

                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId());  // 从 token 中获取用户ID

                HashMap<String, Object> resultMap = userService.updateUserName(userId, name);
                if ("用户名更新成功".equals(resultMap.get("msg"))) {
                    return R.ok("用户名更新成功");
                } else {
                    return R.error((String) resultMap.get("msg"));
                }
            } catch (Exception e) {
                return R.error(e.toString());
            }
        }

        @ApiOperation("修改用户头像头像接口")
        @RequestMapping(value = "/updateUserAvatar", method = RequestMethod.GET)
        public R updateUserAvatar(@RequestParam("avatar") MultipartFile profilePictureFile, @RequestParam("userid") int userid){
            if (profilePictureFile.isEmpty()) {
                return R.error();
            }
            String filePath = fileService.updateFile(profilePictureFile);
            userService.updateUserAvatar(userid,filePath);
            return null;
        }

        @PostMapping(value = "/updateUserBirthTime")
        @ApiOperation("修改用户生日接口")
        public R updateUserBirthTime(
                @RequestHeader("Authorization") String token,  // 从请求头获取 JWT token
                @RequestParam(required = false) LocalDate birthTime) {

            try {
                // 验证 token 并获取 userId
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }

                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId

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
        public R updateUsercompany(@RequestHeader("Authorization") String token,
                                   @RequestParam(required = false) String company)
        {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId

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
        public R updateUserAcademicField(@RequestHeader("Authorization") String token,
                                         @RequestParam(required = false) AcademicFieldType academicField)
        {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId
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
        public R updateUserProfession(@RequestHeader("Authorization") String token,
                                      @RequestParam(required = false) String profession)
        {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId
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
        public R updateUserPhone(@RequestHeader("Authorization") String token,
                                 @RequestParam(required = false) String phone)
        {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId
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
                @RequestHeader("Authorization") String token,
                @RequestParam String oldPassword,
                @RequestParam String newPassword) {
            try {
                CheckResult checkResult = JwtUtils.validateJWT(token);
                if (!checkResult.isSuccess()) {
                    return R.error("Token无效或已过期");
                }
                Claims claims = checkResult.getClaims();
                int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId
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

