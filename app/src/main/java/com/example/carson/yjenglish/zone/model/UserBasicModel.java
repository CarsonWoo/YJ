package com.example.carson.yjenglish.zone.model;

/**
 * Created by 84594 on 2018/9/20.
 */

public class UserBasicModel {

    private String token;
    private String username;
    private String gender;
    private String personality_signature;

    public UserBasicModel(String token, String username, String gender, String personality_signature) {
        this.token = token;
        this.username = username;
        this.gender = gender;
        this.personality_signature = personality_signature;
    }

    public String getPersonality_signature() {
        return personality_signature;
    }

    public String getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
