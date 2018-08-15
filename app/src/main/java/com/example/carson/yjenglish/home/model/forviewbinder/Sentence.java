package com.example.carson.yjenglish.home.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/14.
 */

public class Sentence {
    private String sentence;
    private String sentenceTrans;
    private String sentenceSound;
    public Sentence(String sentence, String sentenceTrans, String sentenceSound) {
        this.sentence = sentence;
        this.sentenceTrans = sentenceTrans;
        this.sentenceSound = sentenceSound;
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
}
