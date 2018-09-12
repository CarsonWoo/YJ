package com.example.carson.yjenglish.checkcode.model;

/**
 * Created by 84594 on 2018/8/30.
 */

public class RegisterCodeBean {
    private String register_token;
    private String phone_code;

    public RegisterCodeBean(String register_token, String phone_code) {
        this.register_token = register_token;
        this.phone_code = phone_code;
    }

    public String getRegister_token() {
        return register_token;
    }

    public void setRegister_token(String register_token) {
        this.register_token = register_token;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }
}
