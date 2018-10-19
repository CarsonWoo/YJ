package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/18.
 */

public class Recommend {
    private String id;
    private String title;
    private String imgUrl;
    private String video_url;
    private String likes;
    private String portraitUrl;
    private String username;
    private String tag;

    public Recommend(String id, String title, String imgUrl, String video_url, String likes,
                     String portraitUrl, String username, String tag) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.video_url = video_url;
        this.likes = likes;
        this.portraitUrl = portraitUrl;
        this.username = username;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getTag() {
        return tag;
    }

    public String getId() {
        return id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getLikes() {
        return likes;
    }
}
