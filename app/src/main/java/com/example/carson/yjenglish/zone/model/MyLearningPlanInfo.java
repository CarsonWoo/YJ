package com.example.carson.yjenglish.zone.model;

import java.util.List;

/**
 * Created by 84594 on 2018/9/5.
 */

public class MyLearningPlanInfo {

    private String status;
    private String msg;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String selected_plan;
        private List<WordInfo> have_plan;

        public List<WordInfo> getHave_plan() {
            return have_plan;
        }

        public void setHave_plan(List<WordInfo> have_plan) {
            this.have_plan = have_plan;
        }

        public String getSelected_plan() {
            return selected_plan;
        }

        public void setSelected_plan(String selected_plan) {
            this.selected_plan = selected_plan;
        }

        public class WordInfo {
            private String word_number;
            private String plan;
            private boolean isLearning;
            private int progress;
            private boolean isEditing;

            public String getWord_number() {
                return word_number;
            }

            public void setWord_number(String word_number) {
                this.word_number = word_number;
            }

            public String getPlan() {
                return plan;
            }

            public void setPlan(String plan) {
                this.plan = plan;
            }

            public boolean isLearning() {
                return isLearning;
            }

            public void setLearning(boolean learning) {
                isLearning = learning;
            }

            public int getProgress() {
                return progress;
            }

            public void setProgress(int progress) {
                this.progress = progress;
            }

            public boolean isEditing() {
                return isEditing;
            }

            public void setEditing(boolean editing) {
                isEditing = editing;
            }
        }

    }

}
