package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class Text {

    private String text;
    public boolean hasDrawable;
    public Text(String text, boolean hasDrawable) {
        this.text = text;
        this.hasDrawable = hasDrawable;
    }

    public String getText() {
        return text;
    }
}
