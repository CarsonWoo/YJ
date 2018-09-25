package com.example.carson.yjenglish.zone.model;

import java.util.List;

/**
 * Created by 84594 on 2018/9/13.
 */

public class MyPlanDailyInfo {
    private String status;
    private String msg;
    private List<MyPlanDaily> data;

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

    public List<MyPlanDaily> getData() {
        return data;
    }

    public void setData(List<MyPlanDaily> data) {
        this.data = data;
    }


    /**
     * 返回词数和天数
     */
    public class MyPlanDaily {
        private String days;
        private String daily_word_number;
        private String date;

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getDaily_word_number() {
            return daily_word_number;
        }

        public void setDaily_word_number(String daily_word_number) {
            this.daily_word_number = daily_word_number;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }





}
