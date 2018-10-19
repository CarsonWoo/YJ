package com.example.carson.yjenglish.msg.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/18.
 */

public class FavoursInfo {

    private String status;
    private String msg;
    private List<LikeMsg> data;

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

    public List<LikeMsg> getData() {
        return data;
    }

    public void setData(List<LikeMsg> data) {
        this.data = data;
    }
}
