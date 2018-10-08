package com.example.carson.yjenglish.zone.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/4.
 */

public class OtherUsersPlanInfo {

    private String status;
    private String msg;
    private OtherUserInfo data;

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

    public OtherUserInfo getData() {
        return data;
    }

    public void setData(OtherUserInfo data) {
        this.data = data;
    }

    public class OtherUserInfo {
        private String author_gender;
        private String learned_word;
        private String author_username;
        private String author_personality_signature;
        private String author_portrait;
        private String author_id;
        private List<OtherPlanInfo> its_plan;

        public String getAuthor_gender() {
            return author_gender;
        }

        public void setAuthor_gender(String author_gender) {
            this.author_gender = author_gender;
        }

        public String getLearned_word() {
            return learned_word;
        }

        public void setLearned_word(String learned_word) {
            this.learned_word = learned_word;
        }

        public String getAuthor_personality_signature() {
            return author_personality_signature;
        }

        public void setAuthor_personality_signature(String author_personality_signature) {
            this.author_personality_signature = author_personality_signature;
        }

        public String getAuthor_portrait() {
            return author_portrait;
        }

        public void setAuthor_portrait(String author_portrait) {
            this.author_portrait = author_portrait;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public List<OtherPlanInfo> getIts_plan() {
            return its_plan;
        }

        public void setIts_plan(List<OtherPlanInfo> its_plan) {
            this.its_plan = its_plan;
        }

        public String getAuthor_username() {
            return author_username;
        }

        public void setAuthor_username(String author_username) {
            this.author_username = author_username;
        }

        public class OtherPlanInfo {
            private String word_number;
            private String learned_word_number;
            private String plan;

            public String getWord_number() {
                return word_number;
            }

            public void setWord_number(String word_number) {
                this.word_number = word_number;
            }

            public String getLearned_word_number() {
                return learned_word_number;
            }

            public void setLearned_word_number(String learned_word_number) {
                this.learned_word_number = learned_word_number;
            }

            public String getPlan() {
                return plan;
            }

            public void setPlan(String plan) {
                this.plan = plan;
            }
        }

    }

}
