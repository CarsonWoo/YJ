package com.example.carson.yjenglish.zone.model;

/**
 * Created by 84594 on 2018/9/19.
 */

public class ZoneInfo {
    private String status;
    private String msg;
    private UserInfo data;

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

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public class UserInfo {
        private String gender;//性别
        private String personality_signature;
        private String portrait;
        private String user_id;
        private String username;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPersonality_signature() {
            return personality_signature;
        }

        public void setPersonality_signature(String personality_signature) {
            this.personality_signature = personality_signature;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
