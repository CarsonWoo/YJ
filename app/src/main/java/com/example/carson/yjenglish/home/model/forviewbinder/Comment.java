package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/20.
 */

public class Comment {

    private String username;
    private String portraitUrl;
    private String comment;
    private String content;
    private int likeNum;
    private Reply mReply;
    private boolean hasReply;
    private boolean isAuthorReplied;
    private String time;

    public Comment(String username, String portraitUrl, String time, String comment, String content, int likeNum,
                   Reply reply) {
        this.username = username;
        this.portraitUrl = portraitUrl;
        this.comment = comment;
        this.content = content;
        this.likeNum = likeNum;
        this.mReply = reply;
        this.time = time;
        if (reply == null) {
            hasReply = false;
        } else {
            hasReply = true;
        }
        if (content == null) {
            isAuthorReplied = false;
        } else {
            isAuthorReplied = true;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public String getComment() {
        return comment;
    }

    public String getContent() {
        return content;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public Reply getReply() {
        return mReply;
    }

    public boolean isHasReply() {
        return hasReply;
    }

    public boolean isAuthorReplied() {
        return isAuthorReplied;
    }

    public String getTime() {
        return time;
    }

    public static class Reply {
        private String username;
        private String portraitUrl;
        private String reply;
        private int likeNum;
        private String time;

        public Reply(String username, String portraitUrl, String time, String reply, int likeNum) {
            this.username = username;
            this.portraitUrl = portraitUrl;
            this.reply = reply;
            this.likeNum = likeNum;
            this.time = time;
        }

        public String getUsername() {
            return username;
        }

        public String getPortraitUrl() {
            return portraitUrl;
        }

        public String getReply() {
            return reply;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public String getTime() {
            return time;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setPortraitUrl(String portraitUrl) {
            this.portraitUrl = portraitUrl;
        }
    }

}
