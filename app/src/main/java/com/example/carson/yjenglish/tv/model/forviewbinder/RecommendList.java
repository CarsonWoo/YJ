package com.example.carson.yjenglish.tv.model.forviewbinder;

import java.util.List;

/**
 * Created by 84594 on 2018/10/1.
 */

public class RecommendList {
    private List<TVRecommendation> mList;
    public RecommendList(List<TVRecommendation> list) {
        this.mList = list;
    }

    public List<TVRecommendation> getList() {
        return mList;
    }
}
