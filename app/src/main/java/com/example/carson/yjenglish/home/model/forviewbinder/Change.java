package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/14.
 */

public class Change {
    private String former;
    private String text;
    public Change(String former, String text) {
        this.former = former;
        this.text = text;
    }

    public String getFormer() {
        return former;
    }

    public String getText() {
        return text;
    }
}
