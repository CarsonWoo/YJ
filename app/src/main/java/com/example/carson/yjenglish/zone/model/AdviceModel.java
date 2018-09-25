package com.example.carson.yjenglish.zone.model;

/**
 * Created by 84594 on 2018/9/24.
 */

public class AdviceModel {

    private String advice;
    private String level;

    public AdviceModel(String advice, String level) {
        this.advice = advice;
        this.level = level;
    }

    public String getAdvice() {
        return advice;
    }

    public String getLevel() {
        return level;
    }
}
