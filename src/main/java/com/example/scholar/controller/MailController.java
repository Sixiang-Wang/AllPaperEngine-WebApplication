package com.example.scholar.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.scholar.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
public class MailController {
    @Resource
    MailUtil mailUtil = new MailUtil();

    @RequestMapping(value = "/sendMail",method = RequestMethod.GET)
    public Object sendMail(HttpServletRequest request){
        String to = request.getParameter("to").trim();
        JSONObject jsonObject = new JSONObject();
        String verifyCode = mailUtil.sendMail(to);
        jsonObject.put("verifyCode",verifyCode);
        System.out.println(to+":"+verifyCode);
        return jsonObject;
    }
}
