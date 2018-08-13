package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class Header {
    private String word;
    private String basicTrans;
    private String soundMark;
    private String soundUrl;

    public Header(String word, String basicTrans, String soundMark, String soundUrl) {
        this.word = word;
        this.basicTrans = basicTrans;
        this.soundMark = soundMark;
        this.soundUrl = soundUrl;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getBasicTrans() {
        return basicTrans;
    }

    public void setBasicTrans(String basicTrans) {
        this.basicTrans = basicTrans;
    }

    public String getSoundMark() {
        return soundMark;
    }

    public void setSoundMark(String soundMark) {
        this.soundMark = soundMark;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }
}
