package com.example.carson.yjenglish.zone.model.forviewbinder;

/**
 * Created by 84594 on 2018/9/10.
 */

public class UserActive {
    private int type;//{0:喜欢：; 1:评论：; 2:回复某人
    private String username;
    private String portrait_url;
    private String other_username;
    private String text;
    private int itemType;//0 - 4 代表home tv music word daily

    private HomeFeeds homeFeeds;
    private TVFeeds tvFeeds;
    private MusicItem musicItem;
    private WordItem wordItem;
    private DailyCardItem dailyCardItem;

    //喜欢可用此
    public UserActive(int itemType, int type, String username, String portrait_url) {
        this(itemType, type, username, portrait_url, null);
    }

    //评论可用此
    public UserActive(int itemType, int type, String username, String portrait_url, String text) {
        this(itemType, type, username, portrait_url, text, null);
    }

    //回复可用此
    public UserActive(int itemType, int type, String username, String portrait_url, String text, String other_username) {
        this.type = type;
        this.username = username;
        this.portrait_url = portrait_url;
        this.text = text;
        this.other_username = other_username;
        this.itemType = itemType;
    }

    public int getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getPortrait_url() {
        return portrait_url;
    }

    public String getOther_username() {
        return other_username;
    }

    public String getText() {
        return text;
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

    public MusicItem getMusicItem() {
        return musicItem;
    }

    public void setMusicItem(MusicItem musicItem) {
        this.musicItem = musicItem;
    }

    public WordItem getWordItem() {
        return wordItem;
    }

    public void setWordItem(WordItem wordItem) {
        this.wordItem = wordItem;
    }

    public DailyCardItem getDailyCardItem() {
        return dailyCardItem;
    }

    public void setDailyCardItem(DailyCardItem dailyCardItem) {
        this.dailyCardItem = dailyCardItem;
    }

    public int getItemType() {
        return itemType;
    }
}
