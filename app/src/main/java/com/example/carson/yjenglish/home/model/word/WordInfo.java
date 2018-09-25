package com.example.carson.yjenglish.home.model.word;

import java.util.List;

/**
 * Created by 84594 on 2018/9/15.
 */

public class WordInfo {
    private String status;
    private String msg;
    private ListObject data;

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

    public ListObject getData() {
        return data;
    }

    public void setData(ListObject data) {
        this.data = data;
    }

    public class ListObject {
        private List<WordData> old_list;
        private List<WordData> new_list;

        public List<WordData> getOld_list() {
            return old_list;
        }

        public void setOld_list(List<WordData> old_list) {
            this.old_list = old_list;
        }

        public List<WordData> getNew_list() {
            return new_list;
        }

        public void setNew_list(List<WordData> new_list) {
            this.new_list = new_list;
        }


        public class WordData {
            private String id;
            private String sentence;
            private String phonetic_symbol_us;//美式音标
            private String pronunciation_us;//美式发音
            private String sentence_audio;//句子音频
            private String level;//新单词统一为0 旧单词视情况而定
            private String pic;//单词图片
            private String synonym;//同义词
            private String phrase;//短语
            private String sentence_cn;//英文句子的翻译
            private String meaning;//意思
            private String word;//单词
            private String paraphrase;//同意翻译
            private String right_time;//正确的时间

            public String getSentence() {
                return sentence;
            }

            public void setSentence(String sentence) {
                this.sentence = sentence;
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

            public String getSentence_audio() {
                return sentence_audio;
            }

            public void setSentence_audio(String sentence_audio) {
                this.sentence_audio = sentence_audio;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
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

            public String getSentence_cn() {
                return sentence_cn;
            }

            public void setSentence_cn(String sentence_cn) {
                this.sentence_cn = sentence_cn;
            }

            public String getMeaning() {
                return meaning;
            }

            public void setMeaning(String meaning) {
                this.meaning = meaning;
            }

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }

            public String getParaphrase() {
                return paraphrase;
            }

            public void setParaphrase(String paraphrase) {
                this.paraphrase = paraphrase;
            }

            public String getRight_time() {
                return right_time;
            }

            public void setRight_time(String right_time) {
                this.right_time = right_time;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
