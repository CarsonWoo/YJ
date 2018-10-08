package com.example.carson.yjenglish.home.model;

import java.util.List;

/**
 * Created by 84594 on 2018/9/28.
 */

public class AuthorInfo {
    private String status;
    private String msg;
    private AuthorDetail data;

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

    public AuthorDetail getData() {
        return data;
    }

    public void setData(AuthorDetail data) {
        this.data = data;
    }

    public class AuthorDetail {
        private String author_gender;
        private String author_personality_signature;
        private String author_portrait;
        private String author_id;
        private String author_username;
        private List<HomeInfo.Data.Feed> feeds;

        public String getAuthor_gender() {
            return author_gender;
        }

        public void setAuthor_gender(String author_gender) {
            this.author_gender = author_gender;
        }

        public String getAuthor_personality_signature() {
            return author_personality_signature;
        }

        public void setAuthor_personality_signature(String author_personality_signature) {
            this.author_personality_signature = author_personality_signature;
        }

        public String getAuthor_portrait() {
            return author_portrait;
        }

        public void setAuthor_portrait(String author_portrait) {
            this.author_portrait = author_portrait;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public String getAuthor_username() {
            return author_username;
        }

        public void setAuthor_username(String author_username) {
            this.author_username = author_username;
        }

        public List<HomeInfo.Data.Feed> getFeeds() {
            return feeds;
        }

        public void setFeeds(List<HomeInfo.Data.Feed> feeds) {
            this.feeds = feeds;
        }


    }
}
