package com.example.carson.yjenglish.zone.model;

import java.util.List;

/**
 * Created by 84594 on 2018/9/2.
 */

public class PlanInfo {
    private String status;
    private String msg;
    private List<Data> data;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }


    public class Data {
        private String plan;
        private int word_number;

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public int getWord_number() {
            return word_number;
        }

        public void setWord_number(int word_number) {
            this.word_number = word_number;
        }
    }
}
