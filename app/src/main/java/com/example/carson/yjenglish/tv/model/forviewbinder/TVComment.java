package com.example.carson.yjenglish.tv.model.forviewbinder;

/**
 * Created by 84594 on 2018/10/2.
 */

public class TVComment {
    private String time;
    private String is_like;
    private String comment;
    private String likes;
    private String id;
    private String user_id;
    private String portrait;
    private String username;

    public TVComment(String time, String is_like, String comment, String likes, String id, String user_id, String portrait, String username) {
        this.time = time;
        this.is_like = is_like;
        this.comment = comment;
        this.likes = likes;
        this.id = id;
        this.user_id = user_id;
        this.portrait = portrait;
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public String getIs_like() {
        return is_like;
    }

    public String getComment() {
        return comment;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getUsername() {
        return username;
    }

    public String getLikes() {
        return likes;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
