package com.example.carson.yjenglish.login.model;

/**
 * Created by 84594 on 2018/7/28.
 */

public class LoginModule {
    //TODO 要将username替换成对应的key
    private String username;
    private String password;

    public LoginModule(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
