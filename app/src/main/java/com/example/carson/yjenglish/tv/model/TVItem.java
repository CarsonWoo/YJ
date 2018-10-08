package com.example.carson.yjenglish.tv.model;

/**
 * Created by 84594 on 2018/8/3.
 */

public class TVItem {

    private String word_id;
    private String videoUrl;
    private String coverImg;
    private String word;
    private String soundMark;
    private String commentNum;
    private String likeNum;
    private String playNum;
    private String video_id;
    private boolean isFavour;

    public TVItem(String word_id, String videoUrl, String coverImg, String word, String soundMark, String commentNum, String likeNum, String playNum, String video_id, boolean isFavour) {
        this.word_id = word_id;
        this.videoUrl = videoUrl;
        this.coverImg = coverImg;
        this.word = word;
        this.soundMark = soundMark;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.playNum = playNum;
        this.video_id = video_id;
        this.isFavour = isFavour;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public void setPlayNum(String playNum) {
        this.playNum = playNum;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setFavour(boolean favour) {
        isFavour = favour;
    }

    public String getWord() {
        return word;
    }

    public String getSoundMark() {
        return soundMark;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public String getWord_id() {
        return word_id;
    }

    public String getPlayNum() {
        return playNum;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public boolean isFavour() {
        return isFavour;
    }

    public String getVideo_id() {
        return video_id;
    }
}
