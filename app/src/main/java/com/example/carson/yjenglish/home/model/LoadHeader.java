package com.example.carson.yjenglish.home.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by 84594 on 2018/8/1.
 */

public class LoadHeader extends DataSupport {

    @Column(defaultValue = "1")
    private int header_id;
    @Column(defaultValue = "0")
    private int wordsCount;
    @Column(defaultValue = "0")
    private int targetCount;
    @Column(defaultValue = "0")
    private int countDown;
    @Column(defaultValue = "0")
    private int insistCount;
    @Column(defaultValue = "false")
    private boolean isTodayFinish;
    @Column(defaultValue = "false")
    private boolean isSignClick;
    private float progress;

    public int getWordsCount() {
        return wordsCount;
    }

    public void setWordsCount(int wordsCount) {
        this.wordsCount = wordsCount;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public int getInsistCount() {
        return insistCount;
    }

    public void setInsistCount(int insistCount) {
        this.insistCount = insistCount;
    }

    public boolean isTodayFinish() {
        return isTodayFinish;
    }

    public void setTodayFinish(boolean todayFinish) {
        isTodayFinish = todayFinish;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public boolean isSignClick() {
        return isSignClick;
    }

    public void setSignClick(boolean signClick) {
        isSignClick = signClick;
    }

    public int getHeader_id() {
        return header_id;
    }

    public void setHeader_id(int header_id) {
        this.header_id = header_id;
    }
}
