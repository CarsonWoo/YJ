package com.example.carson.yjenglish.login.model;

/**
 * Created by 84594 on 2018/7/31.
 */

public class ForgetModel {
    private String forget_password_token;
    private String password;
    public ForgetModel(String forget_password_token, String password) {
        this.password = password;
        this.forget_password_token = forget_password_token;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForget_password_token() {
        return forget_password_token;
    }

    public void setForget_password_token(String forget_password_token) {
        this.forget_password_token = forget_password_token;
    }
}
