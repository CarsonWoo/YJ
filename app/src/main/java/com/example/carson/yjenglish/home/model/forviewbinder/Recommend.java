package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/18.
 */

public class Recommend {
    private String title;
    private String imgUrl;
    private String portraitUrl;
    private String username;
    private String tag;

    public Recommend(String title, String imgUrl, String portraitUrl, String username, String tag) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.portraitUrl = portraitUrl;
        this.username = username;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getTag() {
        return tag;
    }
}
