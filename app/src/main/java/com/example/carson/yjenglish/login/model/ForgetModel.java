package com.example.carson.yjenglish.login.model;

/**
 * Created by 84594 on 2018/7/31.
 */

public class ForgetModel {
    private String phone;
    private String password;
    private int code;
    public ForgetModel(String phone, String password, int code) {
        this.password = password;
        this.phone = phone;
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
