package com.example.scholar.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.scholar.service.MailService;
import com.example.scholar.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class MailController {
    @Resource
    private MailService mailService;

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public JSONObject sendMail(@RequestParam("to") String to) {
        JSONObject jsonObject = new JSONObject();
        Object verifyCode = mailService.sendMail(to);

        jsonObject.put("verifyCode", verifyCode);
        System.out.println(to + ":" + verifyCode);
        return jsonObject;
    }
    @RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
    public JSONObject verifyCode(@RequestParam("to") String to, @RequestParam("verifyCode") String verifyCode) {
        JSONObject result = mailService.verifyCode(to, verifyCode);
        return result;
    }


}
