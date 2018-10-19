package com.example.carson.yjenglish.zone.model;

import com.example.carson.yjenglish.zone.model.forviewbinder.DailyCardItem;
import com.example.carson.yjenglish.zone.model.forviewbinder.HomeFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.MusicItem;
import com.example.carson.yjenglish.zone.model.forviewbinder.TVFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordItem;

/**
 * Created by 84594 on 2018/9/26.
 */

public class CommentModel {
    private String other_username;
    private String reply_comment;
    private String comment;
    private int itemType;//0 - 4 代表home tv music word daily

    private HomeFeeds homeFeeds;
    private TVFeeds tvFeeds;

    //只评论
    public CommentModel(String comment, int itemType) {
        this.comment = comment;
        this.itemType = itemType;
    }

    //回复他人
    public CommentModel(String other_username, String reply_comment, String comment, int itemType) {
        this.other_username = other_username;
        this.reply_comment = reply_comment;
        this.comment = comment;
        this.itemType = itemType;
    }

    public String getOther_username() {
        return other_username;
    }

    public void setOther_username(String other_username) {
        this.other_username = other_username;
    }

    public String getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(String reply_comment) {
        this.reply_comment = reply_comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public HomeFeeds getHomeFeeds() {
        return homeFeeds;
    }

    public void setHomeFeeds(HomeFeeds homeFeeds) {
        this.homeFeeds = homeFeeds;
    }

    public TVFeeds getTvFeeds() {
        return tvFeeds;
    }

    public void setTvFeeds(TVFeeds tvFeeds) {
        this.tvFeeds = tvFeeds;
    }

}
