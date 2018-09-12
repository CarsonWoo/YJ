package com.example.carson.yjenglish.register.model;

/**
 * Created by 84594 on 2018/7/29.
 */

public class RegisterModel {
    private String phone;
    private String token;//填sendCode
    public RegisterModel(String token, String phone) {
        this.token = token;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
