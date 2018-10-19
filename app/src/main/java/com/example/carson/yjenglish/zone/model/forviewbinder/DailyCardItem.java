package com.example.carson.yjenglish.zone.model.forviewbinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class DailyCardItem {
    private String id;
    private String smallImgUrl;
    private String imgUrl;
    private String time;

    public DailyCardItem(String id, String smallImgUrl, String imgUrl, String time) {
        this.id = id;
        this.smallImgUrl = smallImgUrl;
        this.imgUrl = imgUrl;
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTime() {
        return time;
    }

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public String getId() {
        return id;
    }
}
