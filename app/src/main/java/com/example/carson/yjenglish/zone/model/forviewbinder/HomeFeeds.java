package com.example.carson.yjenglish.zone.model.forviewbinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class HomeFeeds {
    private String id;
    private String imgUrl;
    private String title;
    private String portraitUrl;
    private String authorName;
    private String time;

    public HomeFeeds(String id, String imgUrl, String title, String portraitUrl, String authorName, String time) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.title = title;
        this.portraitUrl = portraitUrl;
        this.authorName = authorName;
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }
}
