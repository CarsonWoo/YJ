package com.example.carson.yjenglish.home.model;

import java.util.List;
import java.util.Map;

/**
 * Created by 84594 on 2018/10/18.
 */

public class CalendarInfo {

    private String status;
    private String msg;
    private Map<String, List<Integer>> data;

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

    public Map<String, List<Integer>> getData() {
        return data;
    }

    public void setData(Map<String, List<Integer>> data) {
        this.data = data;
    }
}
