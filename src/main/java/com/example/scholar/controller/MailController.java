package com.example.scholar.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.scholar.service.MailService;
import com.example.scholar.util.MailUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
public class MailController {
    @Resource
    MailUtil mailUtil = new MailUtil();

    @Resource
    private MailService mailService;

    @RequestMapping(value = "/sendMail",method = RequestMethod.GET)
    public Object sendVerifyMail(@RequestParam("to")String to){
        JSONObject jsonObject = new JSONObject();
        String verifyCode = mailUtil.sendVerifyMail(to);
        jsonObject.put("verifyCode",verifyCode);
        System.out.println(to+":"+verifyCode);
        System.out.println("别他妈改我的接口");
        return jsonObject;
    }


    @RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
    public JSONObject verifyCode(@RequestParam("to") String to, @RequestParam("verifyCode") String verifyCode) {
        JSONObject result = mailService.verifyCode(to, verifyCode);
        return result;
    }

}
