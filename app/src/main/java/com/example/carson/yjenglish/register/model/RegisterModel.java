package com.example.carson.yjenglish.register.model;

/**
 * Created by 84594 on 2018/7/29.
 */

public class RegisterModel {
    private String phone;
    private String password;
    public RegisterModel(String phone, String password) {
        this.password = password;
        this.phone = phone;
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
}
