package com.example.carson.yjenglish.home.model;

/**
 * Created by 84594 on 2018/8/1.
 */

public class HomeItem {

    private String title;
    private String videoUrl;
    private String author_username;
    private String comments;
    private String likes;
    private String cover_page;
    private String author_portrait;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAuthor_username() {
        return author_username;
    }

    public void setAuthor_username(String author_username) {
        this.author_username = author_username;
    }

    public String getCover_page() {
        return cover_page;
    }

    public void setCover_page(String cover_page) {
        this.cover_page = cover_page;
    }

    public String getAuthor_portrait() {
        return author_portrait;
    }

    public void setAuthor_portrait(String author_portrait) {
        this.author_portrait = author_portrait;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
