package com.example.carson.yjenglish.discover.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/4.
 */

public class DailyCardInfo {
    private String status;
    private String msg;
    private List<DiscoverInfo.DiscoverItem.DailyCard> data;

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

    public List<DiscoverInfo.DiscoverItem.DailyCard> getData() {
        return data;
    }

    public void setData(List<DiscoverInfo.DiscoverItem.DailyCard> data) {
        this.data = data;
    }
}
