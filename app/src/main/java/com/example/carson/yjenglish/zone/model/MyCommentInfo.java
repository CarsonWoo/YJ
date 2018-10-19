package com.example.carson.yjenglish.zone.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/11.
 */

public class MyCommentInfo {
    private String status;
    private String msg;
    private MyComment data;

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

    public MyComment getData() {
        return data;
    }

    public void setData(MyComment data) {
        this.data = data;
    }

    public class MyComment {
        private List<VideoComment> video_comment;

        private List<FeedComment> feeds_comment;

        public List<VideoComment> getVideo_comment() {
            return video_comment;
        }

        public void setVideo_comment(List<VideoComment> video_comment) {
            this.video_comment = video_comment;
        }

        public List<FeedComment> getFeeds_comment() {
            return feeds_comment;
        }

        public void setFeeds_comment(List<FeedComment> feeds_comment) {
            this.feeds_comment = feeds_comment;
        }

        public class VideoComment {
            private String img;
            private String word_id;
            private String set_time;
            private String phonetic_symbol;
            private String comment;
            private String word;
            private String views;
            private String video_id;

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

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
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
        }

        public class FeedComment {
            private String set_time;
            private String comment;
            private String author_portrait;
            private String pic;
            private String video;
            private String id;
            private String type;
            private String title;
            private String author_id;
            private String author_username;

            public String getSet_time() {
                return set_time;
            }

            public void setSet_time(String set_time) {
                this.set_time = set_time;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getAuthor_portrait() {
                return author_portrait;
            }

            public void setAuthor_portrait(String author_portrait) {
                this.author_portrait = author_portrait;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
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
        }
    }
}
