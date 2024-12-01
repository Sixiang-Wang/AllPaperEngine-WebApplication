package com.example.scholar.service;

import com.alibaba.fastjson.JSONObject;
import com.example.scholar.util.MailUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public interface MailService {
    public Object sendMail(String to);
    public JSONObject verifyCode(String to, String verifyCode);
}
