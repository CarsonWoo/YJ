package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class Video {
    private String bgUrl;
    private String videoUrl;
    public Video(String bgUrl, String videoUrl) {
        this.bgUrl = bgUrl;
        this.videoUrl = videoUrl;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
