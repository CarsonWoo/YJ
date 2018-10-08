package com.example.carson.yjenglish.tv.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/6.
 */

public class TVMoreInfo {
    private String status;
    private String msg;
    private List<TVInfo.TvVideo.WordVideo> data;

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

    public List<TVInfo.TvVideo.WordVideo> getData() {
        return data;
    }

    public void setData(List<TVInfo.TvVideo.WordVideo> data) {
        this.data = data;
    }
}
