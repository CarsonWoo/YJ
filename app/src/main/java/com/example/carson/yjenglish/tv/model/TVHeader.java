package com.example.carson.yjenglish.tv.model;

/**
 * Created by 84594 on 2018/8/3.
 */

public class TVHeader {
    private String imgUrl;
    private String word;
    private String playNum;
    private String video_id;

    public TVHeader(String video_id, String imgUrl, String word, String playNum) {
        this.video_id = video_id;
        this.imgUrl = imgUrl;
        this.word = word;
        this.playNum = playNum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPlayNum() {
        return playNum;
    }

    public void setPlayNum(String playNum) {
        this.playNum = playNum;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
