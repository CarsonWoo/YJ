package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class Video {
    private String video_id;
    private String bgUrl;
    private String videoUrl;
    public Video(String video_id, String bgUrl, String videoUrl) {
        this.video_id = video_id;
        this.bgUrl = bgUrl;
        this.videoUrl = videoUrl;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideo_id() {
        return video_id;
    }
}
