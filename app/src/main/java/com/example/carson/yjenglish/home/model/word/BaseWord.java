package com.example.carson.yjenglish.home.model.word;

/**
 * Created by 84594 on 2018/8/9.
 */

public class BaseWord /* extends DataSupport */ {
    private String word;
    private String trans;
    private String soundMark;
    private String sentence;
    private String sentenceTrans;
    private String paraphrase;
    private String wordUrl;
    private String wordPronounce;
    private int count;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getSoundMark() {
        return soundMark;
    }

    public void setSoundMark(String soundMark) {
        this.soundMark = soundMark;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentenceTrans() {
        return sentenceTrans;
    }

    public void setSentenceTrans(String sentenceTrans) {
        this.sentenceTrans = sentenceTrans;
    }

    public String getParaphrase() {
        return paraphrase;
    }

    public void setParaphrase(String paraphrase) {
        this.paraphrase = paraphrase;
    }

    public String getWordUrl() {
        return wordUrl;
    }

    public void setWordUrl(String wordUrl) {
        this.wordUrl = wordUrl;
    }

    public String getWordPronounce() {
        return wordPronounce;
    }

    public void setWordPronounce(String wordPronounce) {
        this.wordPronounce = wordPronounce;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
