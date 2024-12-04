package com.example.scholar.controller;


import com.example.scholar.domain.constant.CheckResult;
import com.example.scholar.domain.constant.R;
import com.example.scholar.domain.myenum.AppealReasonType;
import com.example.scholar.service.UserAppealService;
import com.example.scholar.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping(value = "/user/appeal")
public class UserAppealController {
    @Resource
    private UserAppealService userAppealService;
    @PostMapping(value = "/")
    @ApiOperation("申诉接口")
    public R appealWork(@RequestHeader("Authorization") String token,
                        @RequestParam int workId,
                        @RequestParam String name,
                        @RequestParam String company,
                        @RequestParam AppealReasonType appealReasonType,
                        @RequestParam String mail,
                        @RequestParam String reason)
    {
        try {
            CheckResult checkResult = JwtUtils.validateJWT(token);
            if (!checkResult.isSuccess()) {
                return R.error("Token无效或已过期");
            }
            Claims claims = checkResult.getClaims();
            int userId = Integer.parseInt(claims.getId()); // 从 token 中提取 userId

            HashMap<String, Object> resultMap = userAppealService.appealWork(userId, workId, name, company, appealReasonType, mail, reason);

            String msg = (String) resultMap.get("msg");
            if("申诉已提交，请等待管理员审核".equals(msg)) {
                return R.ok(msg).put("user", resultMap.get("user"));
            }
            else {
                return R.error(msg);
            }
        } catch (Exception e) {
            return R.error(e.toString());
        }
    }
}
