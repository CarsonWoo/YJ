package com.example.carson.yjenglish.login.model;

/**
 * Created by 84594 on 2018/7/28.
 */

public class LoginInfo {
    private int code;
    private LoginResponse msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LoginResponse getMsg() {
        return msg;
    }

    public void setMsg(LoginResponse msg) {
        this.msg = msg;
    }


    public class LoginResponse {
        private String token;
        private String usertype;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }
    }
}
