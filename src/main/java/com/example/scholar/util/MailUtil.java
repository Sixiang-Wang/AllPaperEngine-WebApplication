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

    private final String myMailAddress = "2562187628@qq.com";

    @Resource
    private JavaMailSender javaMailSender;

    public String sendVerifyMail(String to){
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

    public void sendAdminMail(String admin) {
        String hashedCode = Md5Utils.agenerate(admin);

        String contentReal = "<div style=\"position: relative; width: 100%; background-color: #001122; text-align: center; padding: 20px; color: white; font-size: 18px;\">\n" +
                "    <p style=\"color:#66FFFF; font-size:20px;\">您好，<br>AllPaper Engine 有管理员申请注册:<br></p>\n" +
                "    <p>请确定管理员申请者</p>\n" +
                "    <p>申请人: <b>" + admin + "</b></p>\n" +
                "    <p style=\"margin: 20px 0;\">\n" +
                "        <a href=\"http://116.204.112.5:1145/admin/agree?admin=" + admin + "&code="+hashedCode+"\"\n" +
                "           style=\"display: inline-block; background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; font-size: 16px; border-radius: 5px;\">\n" +
                "            点击同意\n" +
                "        </a>\n" +
                "    </p>\n" +
                "</div>";


        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage);
            msgHelper.setTo("2562187628@qq.com");
            msgHelper.setSubject("AllPaper Engine 管理员申请: ");

            msgHelper.setText(contentReal, true); // true 表示启用 HTML 格式
            msgHelper.setFrom("AllPaper Engine官方 <" + myMailAddress + ">");
        };
        javaMailSender.send(msg);
    }



    public String getVerifyCode(){
        Integer randomNum = random.nextInt(999999);
        return String.format("%06d",randomNum);
    }

}
