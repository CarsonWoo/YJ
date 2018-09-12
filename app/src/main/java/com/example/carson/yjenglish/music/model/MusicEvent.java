package com.example.carson.yjenglish.music.model;

import com.example.carson.yjenglish.service.MusicService;

/**
 * Created by 84594 on 2018/8/30.
 */

public class MusicEvent {
    private String imgUrl;
    private String title;
    private String lrcText;
    private boolean isPlaying;
    public MusicEvent(String imgUrl, String title, String lrcText, boolean isPlaying) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.lrcText = lrcText;
        this.isPlaying = isPlaying;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLrcText() {
        return lrcText;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
