package com.example.carson.yjenglish;

/**
 * Created by 84594 on 2018/8/11.
 */

public class EmptyValue {
    private String text;
    private int drawableRes;
    public EmptyValue(String text) {
        this.text = text;
    }

    public EmptyValue(String text, int drawableRes) {
        this.text = text;
        this.drawableRes = drawableRes;
    }
    public String getText() {
        return text;
    }

    public int getDrawableRes() {
        return drawableRes;
    }
}
