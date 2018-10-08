package com.example.carson.yjenglish.home.model.forviewbinder;

import android.text.SpannableStringBuilder;

/**
 * Created by 84594 on 2018/8/18.
 */

public class Content {
    private String author_id;
    private String title;
    private String portraitUrl;
    private String username;
    private String likeNum;

    public Content(String author_id, String title, String portraitUrl, String username,
                   String likeNum) {
        this.author_id = author_id;
        this.title = title;
        this.portraitUrl = portraitUrl;
        this.username = username;
        this.likeNum = likeNum;
    }

    public String getTitle() {
        return title;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public String getAuthor_id() {
        return author_id;
    }
}
