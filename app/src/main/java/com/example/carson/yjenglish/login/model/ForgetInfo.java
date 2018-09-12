package com.example.carson.yjenglish.login.model;

/**
 * Created by 84594 on 2018/8/31.
 */

public class ForgetInfo {
    private String status;
    private String msg;
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class Data {
        private String forget_password_token;

        public String getForget_password_token() {
            return forget_password_token;
        }

        public void setForget_password_token(String forget_password_token) {
            this.forget_password_token = forget_password_token;
        }
    }
}
