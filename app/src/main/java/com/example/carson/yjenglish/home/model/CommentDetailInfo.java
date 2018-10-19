package com.example.carson.yjenglish.home.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/14.
 */

public class CommentDetailInfo {
    private String status;
    private String msg;
    private CommentDetail data;

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

    public CommentDetail getData() {
        return data;
    }

    public void setData(CommentDetail data) {
        this.data = data;
    }

    public class CommentDetail {
        private String comments;
        private String is_like;
        private String set_time;
        private String user_id;
        private List<HomeItemInfo.HomeItemData.Comment.InnerComment> inner_comment;
        private String comment;
        private String id;
        private String portrait;
        private String likes;
        private String username;

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public String getSet_time() {
            return set_time;
        }

        public void setSet_time(String set_time) {
            this.set_time = set_time;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public List<HomeItemInfo.HomeItemData.Comment.InnerComment> getInner_comment() {
            return inner_comment;
        }

        public void setInner_comment(List<HomeItemInfo.HomeItemData.Comment.InnerComment> inner_comment) {
            this.inner_comment = inner_comment;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
