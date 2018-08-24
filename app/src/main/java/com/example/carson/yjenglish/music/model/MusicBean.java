package com.example.carson.yjenglish.music.model;

/**
 * Created by 84594 on 2018/8/23.
 */

public class MusicBean {
    private String word;
    private String musicUrl;
    private boolean isCurPlaying;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public boolean isCurPlaying() {
        return isCurPlaying;
    }

    public void setCurPlaying(boolean curPlaying) {
        isCurPlaying = curPlaying;
    }
}
