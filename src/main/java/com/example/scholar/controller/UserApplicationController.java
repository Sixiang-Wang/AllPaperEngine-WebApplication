package com.example.scholar.controller;

import com.example.scholar.domain.constant.CheckResult;
import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.myenum.AcademicFieldType;
import com.example.scholar.dto.LoginDto;
import com.example.scholar.service.UserApplicationService;
import com.example.scholar.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping(value = "/user/application")
public class UserApplicationController {
    @Resource
    private UserApplicationService userApplicationService;
    @PostMapping(value = "/certify")
    @ApiOperation("学者认证申请接口")
    public R certifyScholar(
            @RequestHeader("Authorization") String token,
            @RequestParam String name,
            @RequestParam String company,
            @RequestParam AcademicFieldType academicFieldType,
            @RequestParam String mail,
            @RequestParam String code) {
        try {
            CheckResult checkResult = JwtUtils.validateJWT(token);
            if (!checkResult.isSuccess()) {
                return R.error("Token无效或已过期");
            }
            Claims claims = checkResult.getClaims();
            int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId

             HashMap<String, Object> resultMap = userApplicationService.certifyScholar(userId, name, company, academicFieldType, mail, code);

            String msg = (String) resultMap.get("msg");
            if ("用户不存在".equals(msg)) {
                return R.error("用户不存在");
            } else if ("当前用户已经是学者".equals(msg)) {
                return R.error("当前用户已经是学者");
            } else if ("信息填写不完整".equals(msg)) {
                return R.error("信息填写不完整");
            } else if ("验证码不存在".equals(msg)) {
                return R.error("验证码不存在");
            } else if ("验证码已过期".equals(msg)) {
                return R.error("验证码已过期");
            } else if ("验证码错误，请重新输入".equals(msg)) {
                return R.error("验证码错误，请重新输入");
            } else if ("学者认证申请已提交，请等待管理员审核".equals(msg)) {
                return R.ok(msg).put("user", resultMap.get("user"));
            }
            return R.error("学者认证申请出错");
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }
}
