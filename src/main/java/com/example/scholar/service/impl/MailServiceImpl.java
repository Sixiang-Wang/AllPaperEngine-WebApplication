package com.example.scholar.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.scholar.service.MailService;
import com.example.scholar.util.MailUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("mailService")
public class MailServiceImpl implements MailService {
    @Resource
    MailUtil mailUtil = new MailUtil();
    private Map<String, String> verifyCodes = new HashMap<>();
    private Map<String, Long> verifyCodeTimes = new HashMap<>();
    private final Map<String, Long> lastRequestTimes = new HashMap<>(); // 最后发送的时间

    private static final long EXPIRY_TIME = 60 * 1000;  // 60秒过期
    private static final long SEND_LIMIT_INTERVAL = 60 * 1000;// 发送频率限制为60秒

    // 定时任务线程池
    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    @PostConstruct
    public void init() {
        taskScheduler.initialize();
        taskScheduler.scheduleAtFixedRate(this::cleanExpiredCodes, 5 * 60 * 1000);
    }
    @Override
    public Object sendMail(String to){
        JSONObject jsonObject = new JSONObject();

        Long lastRequestTime = lastRequestTimes.get(to);
        if (lastRequestTime != null && System.currentTimeMillis() - lastRequestTime < SEND_LIMIT_INTERVAL) {
            jsonObject.put("error", "发送过于频繁，请稍后再试！");
            return jsonObject;
        }

        String verifyCode = mailUtil.sendMail(to);
        verifyCodes.put(to,verifyCode);
        verifyCodeTimes.put(to,System.currentTimeMillis() + EXPIRY_TIME);

        jsonObject.put("verifyCode",verifyCode);
        System.out.println(to+":"+verifyCode);
        return jsonObject;
    }

    @Override
    public JSONObject verifyCode(String to, String verifyCode){
        JSONObject jsonObject = new JSONObject();

        String code = verifyCodes.get(to);
        Long time = verifyCodeTimes.get(to);
        if(code == null || time == null){
            jsonObject.put("error", "验证码不存在");
            return jsonObject;
        }

        if(System.currentTimeMillis() > time){
            jsonObject.put("error", "验证码已过期");
            return jsonObject;
        }

        if(verifyCode.equals(code)){
            jsonObject.put("msg", "验证码校验成功");
            verifyCodes.remove(to);
            verifyCodeTimes.remove(to);
        }
        else {
            jsonObject.put("error", "验证码错误，请重新输入");
        }
        return jsonObject;
    }

    private void cleanExpiredCodes() {
        long currentTime = System.currentTimeMillis();

        verifyCodeTimes.entrySet().removeIf(entry -> currentTime > entry.getValue());
        verifyCodes.entrySet().removeIf(entry -> !verifyCodeTimes.containsKey(entry.getKey()));
        lastRequestTimes.entrySet().removeIf(entry -> !verifyCodeTimes.containsKey(entry.getKey()));

        System.out.println("已清理过期验证码和发送记录！");
    }
}
