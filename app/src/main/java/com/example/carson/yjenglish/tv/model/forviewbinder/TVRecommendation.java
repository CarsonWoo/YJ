package com.example.carson.yjenglish.tv.model.forviewbinder;

/**
 * Created by 84594 on 2018/10/1.
 */

public class TVRecommendation {
    private String word;
    private String views;
    private String video_id;
    private String img;

    public TVRecommendation(String word, String views, String video_id, String img) {
        this.word = word;
        this.views = views;
        this.video_id = video_id;
        this.img = img;
    }

    public String getWord() {
        return word;
    }

    public String getViews() {
        return views;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getImg() {
        return img;
    }
}
