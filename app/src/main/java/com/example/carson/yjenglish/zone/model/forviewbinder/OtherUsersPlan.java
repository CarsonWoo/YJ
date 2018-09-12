package com.example.carson.yjenglish.zone.model.forviewbinder;

/**
 * Created by 84594 on 2018/9/10.
 */

public class OtherUsersPlan {
    private String tag;
    private String word_count;
    private int progress;

    public OtherUsersPlan(String tag, String word_count, int progress) {
        this.tag = tag;
        this.word_count = word_count;
        this.progress = progress;
    }

    public String getTag() {
        return tag;
    }

    public String getWord_count() {
        return word_count;
    }

    public int getProgress() {
        return progress;
    }
}
