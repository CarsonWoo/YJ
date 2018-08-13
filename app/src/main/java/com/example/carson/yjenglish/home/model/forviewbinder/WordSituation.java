package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class WordSituation {
    private String imgUrl;
    private String text;
    private String trans;
    private String soundUrl;
    public WordSituation(String imgUrl, String text, String trans, String soundUrl) {
        this.imgUrl = imgUrl;
        this.text = text;
        this.trans = trans;
        this.soundUrl = soundUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getText() {
        return text;
    }

    public String getTrans() {
        return trans;
    }

    public String getSoundUrl() {
        return soundUrl;
    }
}
