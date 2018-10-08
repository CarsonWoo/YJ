package com.example.carson.yjenglish.tv.model;

import java.util.List;

/**
 * Created by 84594 on 2018/9/29.
 */

public class TVInfo {
    private String status;
    private String msg;
    private TvVideo data;

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

    public TvVideo getData() {
        return data;
    }

    public void setData(TvVideo data) {
        this.data = data;
    }

    public class TvVideo {
        private List<TopVideo> top_video;
        private List<WordVideo> word_video;

        public List<TopVideo> getTop_video() {
            return top_video;
        }

        public void setTop_video(List<TopVideo> top_video) {
            this.top_video = top_video;
        }

        public List<WordVideo> getWord_video() {
            return word_video;
        }

        public void setWord_video(List<WordVideo> word_video) {
            this.word_video = word_video;
        }

        public class TopVideo {
            private String img;
            private String word;
            private String video;
            private String views;
            private String video_id;

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

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }
        }

        public class WordVideo {
            private String word_id;
            private String phonetic_symbol;
            private String word;
            private String is_favour;
            private List<VideoInfo> video_info;

            public String getWord_id() {
                return word_id;
            }

            public void setWord_id(String word_id) {
                this.word_id = word_id;
            }

            public String getPhonetic_symbol() {
                return phonetic_symbol;
            }

            public void setPhonetic_symbol(String phonetic_symbol) {
                this.phonetic_symbol = phonetic_symbol;
            }

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }

            public List<VideoInfo> getVideo_info() {
                return video_info;
            }

            public void setVideo_info(List<VideoInfo> video_info) {
                this.video_info = video_info;
            }

            public String getIs_favour() {
                return is_favour;
            }

            public void setIs_favour(String is_favour) {
                this.is_favour = is_favour;
            }

            public class VideoInfo {
                private String img;
                private String comments;//评论数
                private String favours;//喜欢数
                private String video;//视频地址
                private String views;//浏览数
                private String video_id;
                private String is_favour;

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getComments() {
                    return comments;
                }

                public void setComments(String comments) {
                    this.comments = comments;
                }

                public String getFavours() {
                    return favours;
                }

                public void setFavours(String favours) {
                    this.favours = favours;
                }

                public String getVideo() {
                    return video;
                }

                public void setVideo(String video) {
                    this.video = video;
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

                public String getIs_favour() {
                    return is_favour;
                }

                public void setIs_favour(String is_favour) {
                    this.is_favour = is_favour;
                }
            }
        }
    }
}
