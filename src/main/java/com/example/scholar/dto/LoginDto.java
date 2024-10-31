package com.example.scholar.dto;

import lombok.Data;
/*
 * 前端传输为:
 * account: loginForm.value.mail,
 * password: loginForm.value.password,
 *
 * 按照数据库字段修改为：
 *
 * 邮箱 mail
 * 密码 password
 */
@Data
public class LoginDto {
    private String mail;
    private String password;
}
