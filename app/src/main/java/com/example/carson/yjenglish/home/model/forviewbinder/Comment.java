package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/20.
 */

public class Comment {

    private String user_id;
    private String username;
    private String portraitUrl;
    private String comment;
    private String content;
    private int likeNum;
    private Reply mReply;
    private boolean hasReply;
    private boolean isAuthorReplied;
    private String time;
    private String comment_id;
    private boolean isLike;

    public Comment(String user_id, String username, String portraitUrl, String time, String comment, String content, int likeNum,
                   Reply reply, String comment_id, boolean isLike) {
        this.user_id = user_id;
        this.username = username;
        this.portraitUrl = portraitUrl;
        this.comment = comment;
        this.content = content;
        this.likeNum = likeNum;
        this.mReply = reply;
        this.time = time;
        this.comment_id = comment_id;
        this.isLike = isLike;
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

    public String getUser_id() {
        return user_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public static class Reply {

        private String user_id;
        private String username;
        private String portraitUrl;
        private String reply;
        private int likeNum;
        private String time;

        public Reply(String user_id, String username, String portraitUrl, String time, String reply, int likeNum) {
            this.user_id = user_id;
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

        public String getUser_id() {
            return user_id;
        }
    }

}
