package com.example.carson.yjenglish.home.model;

import java.util.List;

/**
 * Created by 84594 on 2018/9/2.
 */

public class HomeInfo {
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
        private String flag;
        private String learned_word;
        private String insist_days;
        private String rest_days;
        private String my_plan;
        private String plan_number;
        private String study_people;
        private List<Feed> feeds;
        private List<String> head_user_portrait;

        public List<Feed> getFeeds() {
            return feeds;
        }

        public void setFeeds(List<Feed> feeds) {
            this.feeds = feeds;
        }

        public String getPlan_number() {
            return plan_number;
        }

        public void setPlan_number(String plan_number) {
            this.plan_number = plan_number;
        }

        public String getMy_plan() {
            return my_plan;
        }

        public void setMy_plan(String my_plan) {
            this.my_plan = my_plan;
        }

        public String getRest_days() {
            return rest_days;
        }

        public void setRest_days(String rest_days) {
            this.rest_days = rest_days;
        }

        public String getInsist_days() {
            return insist_days;
        }

        public void setInsist_days(String insist_days) {
            this.insist_days = insist_days;
        }

        public String getLearned_word() {
            return learned_word;
        }

        public void setLearned_word(String learned_word) {
            this.learned_word = learned_word;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getStudy_people() {
            return study_people;
        }

        public void setStudy_people(String study_people) {
            this.study_people = study_people;
        }

        public List<String> getHead_user_portrait() {
            return head_user_portrait;
        }

        public void setHead_user_portrait(List<String> head_user_portrait) {
            this.head_user_portrait = head_user_portrait;
        }

        public class Feed {
            private String id;
            private String comments;
            private String likes;
            private String title;
            private String pic;
            private String author_username;
            private String author_portrait;
            private String video;
            private String type;
            private String is_favour;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public String getAuthor_portrait() {
                return author_portrait;
            }

            public void setAuthor_portrait(String author_portrait) {
                this.author_portrait = author_portrait;
            }

            public String getAuthor_username() {
                return author_username;
            }

            public void setAuthor_username(String author_username) {
                this.author_username = author_username;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLikes() {
                return likes;
            }

            public void setLikes(String likes) {
                this.likes = likes;
            }

            public String getComments() {
                return comments;
            }

            public void setComments(String comments) {
                this.comments = comments;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIs_favour() {
                return is_favour;
            }

            public void setIs_favour(String is_favour) {
                this.is_favour = is_favour;
            }
        }
    }
}
