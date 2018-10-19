package com.example.carson.yjenglish.tv.model;

import java.util.List;

/**
 * Created by 84594 on 2018/10/2.
 */

public class TVItemInfo {
    private String status;
    private String msg;
    private TVItemDetail data;

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

    public TVItemDetail getData() {
        return data;
    }

    public void setData(TVItemDetail data) {
        this.data = data;
    }

    public class TVItemDetail {
        private String is_favour;
        private TopVideo top_video;
        private List<HotComment> hot_comment;
        private List<HotComment> new_comment;
        private List<RecommendVideo> recommend_video;

        public TopVideo getTop_video() {
            return top_video;
        }

        public void setTop_video(TopVideo top_video) {
            this.top_video = top_video;
        }

        public List<HotComment> getHot_comment() {
            return hot_comment;
        }

        public void setHot_comment(List<HotComment> hot_comment) {
            this.hot_comment = hot_comment;
        }

        public List<RecommendVideo> getRecommend_video() {
            return recommend_video;
        }

        public void setRecommend_video(List<RecommendVideo> recommend_video) {
            this.recommend_video = recommend_video;
        }

        public List<HotComment> getNew_comment() {
            return new_comment;
        }

        public void setNew_comment(List<HotComment> new_comment) {
            this.new_comment = new_comment;
        }

        public String getIs_favour() {
            return is_favour;
        }

        public void setIs_favour(String is_favour) {
            this.is_favour = is_favour;
        }

        public class RecommendVideo {
            private String img;
            private String word_id;
            private String video;
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

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
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

        public class HotComment {
            private String set_time;
            private String user_id;
            private String is_like;
            private String comment;
            private String id;
            private String portrait;
            private String username;
            private String likes;

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

            public String getIs_like() {
                return is_like;
            }

            public void setIs_like(String is_like) {
                this.is_like = is_like;
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

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getLikes() {
                return likes;
            }

            public void setLikes(String likes) {
                this.likes = likes;
            }
        }

        public class TopVideo {
            private List<SubTitle> subtitles;
            private String img;
            private String video;

            public List<SubTitle> getSubtitles() {
                return subtitles;
            }

            public void setSubtitles(List<SubTitle> subtitles) {
                this.subtitles = subtitles;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public class SubTitle {
                private String st;
                private String en;
                private String cn;
                private String et;

                public String getSt() {
                    return st;
                }

                public void setSt(String st) {
                    this.st = st;
                }

                public String getEn() {
                    return en;
                }

                public void setEn(String en) {
                    this.en = en;
                }

                public String getCn() {
                    return cn;
                }

                public void setCn(String cn) {
                    this.cn = cn;
                }

                public String getEt() {
                    return et;
                }

                public void setEt(String et) {
                    this.et = et;
                }
            }
        }
    }
}
