package com.example.carson.yjenglish.home.model.forviewbinder;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;

/**
 * Created by 84594 on 2018/8/18.
 */

public class Content {
    private String title;
    private String portraitUrl;
    private String username;
    private SpannableStringBuilder text;
    private String likeNum;

    public Content(String title, String portraitUrl, String username, SpannableStringBuilder text,
                   String likeNum) {
        this.title = title;
        this.portraitUrl = portraitUrl;
        this.username = username;
        this.text = text;
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

    public SpannableStringBuilder getText() {
        return text;
    }

    public String getLikeNum() {
        return likeNum;
    }
}
