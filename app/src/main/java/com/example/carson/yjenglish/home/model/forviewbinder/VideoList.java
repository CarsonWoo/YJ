package com.example.carson.yjenglish.home.model.forviewbinder;

import java.util.List;

/**
 * Created by 84594 on 2018/8/13.
 */

public class VideoList {
    private List<Video> mList;
    public VideoList(List<Video> list) {
        this.mList = list;
    }

    public List<Video> getmList() {
        return mList;
    }
}
