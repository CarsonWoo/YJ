package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/14.
 */

public class Sentence {
    private String sentence;
    private String sentenceTrans;
    private String sentenceSound;
    private String sentenceOrigin;

    public Sentence(String sentence, String sentenceTrans, String sentenceSound) {
        this.sentenceSound = sentenceSound;
        this.sentence = sentence;
        this.sentenceTrans = sentenceTrans;
    }

    public Sentence(String sentence, String sentenceTrans, String sentenceSound, String sentenceOrigin) {
        this.sentence = sentence;
        this.sentenceTrans = sentenceTrans;
        this.sentenceSound = sentenceSound;
        this.sentenceOrigin = sentenceOrigin;
    }

    public String getSentence() {
        return sentence;
    }

    public String getSentenceTrans() {
        return sentenceTrans;
    }

    public String getSentenceSound() {
        return sentenceSound;
    }

    public String getSentenceOrigin() {
        return sentenceOrigin;
    }
}
