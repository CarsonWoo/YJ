package com.example.carson.yjenglish.home.model.word;

import java.util.List;

/**
 * Created by 84594 on 2018/9/20.
 */

public class WordDetailInfo {
    private String status;
    private String msg;
    private Word data;

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

    public Word getData() {
        return data;
    }

    public void setData(Word data) {
        this.data = data;
    }

    public class Word {
        /**
         * 其他参数可由前一接口获取
         */
        private String word_of_similar_form;//形近词
        private String stem_affix;//词根词缀
        private List<VideoInfo> video_info;

        public String getWord_of_similar_form() {
            return word_of_similar_form;
        }

        public void setWord_of_similar_form(String word_of_similar_form) {
            this.word_of_similar_form = word_of_similar_form;
        }

        public String getStem_affix() {
            return stem_affix;
        }

        public void setStem_affix(String stem_affix) {
            this.stem_affix = stem_affix;
        }

        public List<VideoInfo> getVideo_info() {
            return video_info;
        }

        public void setVideo_info(List<VideoInfo> video_info) {
            this.video_info = video_info;
        }

        public class VideoInfo {
            private String id;//视频id
            private String sentence;
            private String video_name;
            private String img;
            private String video;//视频地址
            private String sentence_audio;//句子的音频
            private String translation;

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getVideo_name() {
                return video_name;
            }

            public void setVideo_name(String video_name) {
                this.video_name = video_name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSentence() {
                return sentence;
            }

            public void setSentence(String sentence) {
                this.sentence = sentence;
            }

            public String getSentence_audio() {
                return sentence_audio;
            }

            public void setSentence_audio(String sentence_audio) {
                this.sentence_audio = sentence_audio;
            }

            public String getTranslation() {
                return translation;
            }

            public void setTranslation(String translation) {
                this.translation = translation;
            }
        }

    }
}
