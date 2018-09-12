package com.example.carson.yjenglish.zone.model.forviewbinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class DailyCardItem {
    private String imgUrl;
    private String time;

    public DailyCardItem(String imgUrl, String time) {
        this.imgUrl = imgUrl;
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTime() {
        return time;
    }
}
