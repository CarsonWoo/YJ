package com.example.carson.yjenglish.tv.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/2.
 */

public class CommentInfo {
    private String status;
    private String msg;
    private List<TVItemInfo.TVItemDetail.HotComment> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<TVItemInfo.TVItemDetail.HotComment> getData() {
        return data;
    }

    public void setData(List<TVItemInfo.TVItemDetail.HotComment> data) {
        this.data = data;
    }
}
