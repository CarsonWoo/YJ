package com.example.carson.yjenglish;

import com.example.carson.yjenglish.VideoCaptionModel;

import java.util.List;

/**
 * Created by 84594 on 2018/10/7.
 */

public class VideoCaptionInfo {
    private String status;
    private String msg;
    private List<VideoCaptionModel> data;

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

    public List<VideoCaptionModel> getData() {
        return data;
    }

    public void setData(List<VideoCaptionModel> data) {
        this.data = data;
    }
}
