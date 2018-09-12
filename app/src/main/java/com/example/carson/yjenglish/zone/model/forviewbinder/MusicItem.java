package com.example.carson.yjenglish.zone.model.forviewbinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class MusicItem {
    private String word;
    private String meaning;
    private String time;

    public MusicItem(String word, String meaning, String time) {
        this.word = word;
        this.meaning = meaning;
        this.time = time;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getTime() {
        return time;
    }
}
