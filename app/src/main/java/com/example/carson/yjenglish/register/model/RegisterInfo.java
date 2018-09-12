package com.example.carson.yjenglish.register.model;

/**
 * Created by 84594 on 2018/7/29.
 */

public class RegisterInfo {
    private String status;
    private String msg;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String register_token;

        public String getRegister_token() {
            return register_token;
        }

        public void setRegister_token(String register_token) {
            this.register_token = register_token;
        }
    }
}
