package com.example.carson.yjenglish.home.model.word;

import java.util.List;

/**
 * Created by 84594 on 2018/8/3.
 */

public class RememberWordInfo {

    private String status;
    private String msg;
    private List<RememberWord> data;

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

    public List<RememberWord> getData() {
        return data;
    }

    public void setData(List<RememberWord> data) {
        this.data = data;
    }

    public class RememberWord {
        private String id;
        private String word;
        private String real_meaning;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getReal_Meaning() {
            return real_meaning;
        }

        public void setReal_Meaning(String real_meaning) {
            this.real_meaning = real_meaning;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
