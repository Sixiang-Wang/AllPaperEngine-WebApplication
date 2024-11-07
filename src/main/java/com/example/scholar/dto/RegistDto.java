package com.example.scholar.dto;

import lombok.Data;

/*
 * 前端传输为：
 * 
 * account: registerForm.value.account,
 * password: registerForm.value.password,
 * mail: registerForm.value.mail,
 * 
 * 按照数据库字段修改为：
 * 
 * 用户名 name
 * 密码 password
 * 邮箱 mail
 */
@Data
public class RegistDto {
    private String name;
    private String password;
    private String mail;
}
