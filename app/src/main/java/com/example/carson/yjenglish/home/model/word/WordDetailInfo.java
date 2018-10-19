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
        private String sentence;
        private String sentence_audio;
        private String sentence_cn;
        private String phonetic_symbol_us;
        private String pronunciation_us;
        private String pic;
        private String synonym;
        private String phrase;
        private String meaning;
        private String paraphrase;
        private String word_of_similar_form;//形近词
        private String stem_affix;//词根词缀
        private String word;
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

        public String getSentence_cn() {
            return sentence_cn;
        }

        public void setSentence_cn(String sentence_cn) {
            this.sentence_cn = sentence_cn;
        }

        public String getPhonetic_symbol_us() {
            return phonetic_symbol_us;
        }

        public void setPhonetic_symbol_us(String phonetic_symbol_us) {
            this.phonetic_symbol_us = phonetic_symbol_us;
        }

        public String getPronunciation_us() {
            return pronunciation_us;
        }

        public void setPronunciation_us(String pronunciation_us) {
            this.pronunciation_us = pronunciation_us;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getSynonym() {
            return synonym;
        }

        public void setSynonym(String synonym) {
            this.synonym = synonym;
        }

        public String getPhrase() {
            return phrase;
        }

        public void setPhrase(String phrase) {
            this.phrase = phrase;
        }

        public String getMeaning() {
            return meaning;
        }

        public void setMeaning(String meaning) {
            this.meaning = meaning;
        }

        public String getParaphrase() {
            return paraphrase;
        }

        public void setParaphrase(String paraphrase) {
            this.paraphrase = paraphrase;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
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
