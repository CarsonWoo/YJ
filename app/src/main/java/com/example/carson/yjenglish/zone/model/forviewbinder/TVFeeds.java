package com.example.carson.yjenglish.zone.model.forviewbinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class TVFeeds {
    private String imgUrl;
    private String viewNum;
    private String tag;
    private String soundMark;
    private String time;

    public TVFeeds(String imgUrl, String viewNum, String tag, String soundMark, String time) {
        this.imgUrl = imgUrl;
        this.viewNum = viewNum;
        this.tag = tag;
        this.soundMark = soundMark;
        this.time = time;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public String getViewNum() {
        return viewNum;
    }

    public String getTag() {
        return tag;
    }

    public String getSoundMark() {
        return soundMark;
    }

    public String getTime() {
        return time;
    }
}
