package com.example.carson.yjenglish.zone.model.forviewbinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class WordItem {
    private String word;
    private String soundMark;
    private String time;
    private String imgUrl;

    public WordItem(String word, String soundMark, String time, String imgUrl) {
        this.word = word;
        this.soundMark = soundMark;
        this.time = time;
        this.imgUrl = imgUrl;
    }


    public String getWord() {
        return word;
    }

    public String getSoundMark() {
        return soundMark;
    }

    public String getTime() {
        return time;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
