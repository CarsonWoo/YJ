package com.example.carson.yjenglish.zone.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/14.
 */

public class UserActiveInfo {

    private String status;
    private String msg;
    private ActiveInfo data;

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

    public ActiveInfo getData() {
        return data;
    }

    public void setData(ActiveInfo data) {
        this.data = data;
    }

    public class ActiveInfo {
        private String username;
        private String portrait;

        private String insist_day;

        private String is_open;

        private List<ItemInfo> its_dynamic;

        public String getIs_open() {
            return is_open;
        }

        public void setIs_open(String is_open) {
            this.is_open = is_open;
        }

        public List<ItemInfo> getIts_dynamic() {
            return its_dynamic;
        }

        public void setIts_dynamic(List<ItemInfo> its_dynamic) {
            this.its_dynamic = its_dynamic;
        }

        public String getInsist_day() {
            return insist_day;
        }

        public void setInsist_day(String insist_day) {
            this.insist_day = insist_day;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public class ItemInfo {
            //单词
            private String pronunciation;
            private String set_time;
            private String sort_time;
            private String phonetic_symbol;
            private String pic;
            private String id;
            private String type;//记录是喜欢什么东西 评论什么东西
            private String word;

            //视频
            private String img;
            private String word_id;
            private String comment;
            private String views;
            private String video_id;

            //首页feeds
            private String author_portrait;
            private String title;
            private String author_id;
            private String author_username;

            //每日一图
            private String small_pic;
            private String daily_pic;

            public String getPronunciation() {
                return pronunciation;
            }

            public void setPronunciation(String pronunciation) {
                this.pronunciation = pronunciation;
            }

            public String getSet_time() {
                return set_time;
            }

            public void setSet_time(String set_time) {
                this.set_time = set_time;
            }

            public String getSort_time() {
                return sort_time;
            }

            public void setSort_time(String sort_time) {
                this.sort_time = sort_time;
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

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
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

            public String getAuthor_id() {
                return author_id;
            }

            public void setAuthor_id(String author_id) {
                this.author_id = author_id;
            }

            public String getAuthor_username() {
                return author_username;
            }

            public void setAuthor_username(String author_username) {
                this.author_username = author_username;
            }

            public String getSmall_pic() {
                return small_pic;
            }

            public void setSmall_pic(String small_pic) {
                this.small_pic = small_pic;
            }

            public String getDaily_pic() {
                return daily_pic;
            }

            public void setDaily_pic(String daily_pic) {
                this.daily_pic = daily_pic;
            }
        }
    }

}
