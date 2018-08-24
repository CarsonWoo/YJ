package com.example.carson.yjenglish.home.model.forviewbinder;

import java.util.List;

/**
 * Created by 84594 on 2018/8/18.
 */

public class RecommendList {

    private List<Recommend> mList;

    public RecommendList(List<Recommend> mList) {
        this.mList = mList;
    }

    public List<Recommend> getList() {
        return mList;
    }
}
