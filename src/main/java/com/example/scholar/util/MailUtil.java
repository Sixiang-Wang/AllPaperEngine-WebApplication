package com.example.scholar.util;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;


@Component
public class MailUtil {
    private final Random random = new Random();

    private final String myMailAddress = "2142703200@qq.com";

    @Resource
    private JavaMailSender javaMailSender;

    public String sendMail(String to){
        String verifyCode = getVerifyCode();

        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage);
            msgHelper.setTo(to);
            msgHelper.setSubject("AllPaper Engine验证码: " + verifyCode);
            msgHelper.setText("<div style='background-color:#001122;color:white;border:1px solid black; padding: 10px; font-size: 18px;'>"
                    + "<p style='color:#66FFFF; font-size:20px;'>您好，<br>这是为您AllPaper Engine账号生成的临时验证码:<br></p>"
                    + "<p style='color:white; font-size:30px;'><b><u>" + verifyCode + "</u></b></p><br>"
                    + "请勿泄露和转发。如非本人操作，您的AllPaper Engine账号可能有安全隐患，请尽快更改您的密码</p></div>", true);
            msgHelper.setFrom("AllPaper Engine官方 <" + myMailAddress + ">");
        };
        javaMailSender.send(msg);
        return verifyCode;
    }

    public void sendCustomMail(String to,String from,String subject,String text){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom(from+" <"+myMailAddress+">");
        javaMailSender.send(msg);
    }

    public String getVerifyCode(){
        Integer randomNum = random.nextInt(999999);
        return String.format("%06d",randomNum);
    }

}
