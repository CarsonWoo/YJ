package com.example.carson.yjenglish.zone.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/12.
 */

public class MyFavourInfo {
    private String status;
    private String msg;
    private List<Favours> data;

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

    public List<Favours> getData() {
        return data;
    }

    public void setData(List<Favours> data) {
        this.data = data;
    }

    public class Favours {
        //单词接口
        private String sort_time;
        private String set_time;
        private String phonetic_symbol;
        private String pic;
        private String id;
        private String type;
        private String word;

        //视频接口
        private String img;
        private String word_id;
        private String views;
        private String video_id;

        //feeds接口
        private String author_portrait;
        private String title;
        private String author_username;

        //daily_pic接口
        private String daily_pic;
        private String small_pic;

        public String getSort_time() {
            return sort_time;
        }

        public void setSort_time(String sort_time) {
            this.sort_time = sort_time;
        }

        public String getSet_time() {
            return set_time;
        }

        public void setSet_time(String set_time) {
            this.set_time = set_time;
        }

        public String getPhonetic_symbol() {
            return phonetic_symbol;
        }

        public void setPhonetic_symbol(String phonetic_symbol) {
            this.phonetic_symbol = phonetic_symbol;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getWord_id() {
            return word_id;
        }

        public void setWord_id(String word_id) {
            this.word_id = word_id;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public String getAuthor_portrait() {
            return author_portrait;
        }

        public void setAuthor_portrait(String author_portrait) {
            this.author_portrait = author_portrait;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor_username() {
            return author_username;
        }

        public void setAuthor_username(String author_username) {
            this.author_username = author_username;
        }

        public String getDaily_pic() {
            return daily_pic;
        }

        public void setDaily_pic(String daily_pic) {
            this.daily_pic = daily_pic;
        }

        public String getSmall_pic() {
            return small_pic;
        }

        public void setSmall_pic(String small_pic) {
            this.small_pic = small_pic;
        }
    }
}
