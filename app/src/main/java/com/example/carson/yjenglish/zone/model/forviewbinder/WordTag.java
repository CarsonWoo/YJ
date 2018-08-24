package com.example.carson.yjenglish.zone.model.forviewbinder;

/**
 * Created by 84594 on 2018/8/17.
 */

public class WordTag {
    private String wordTag;
    private String wordCount;
    public WordTag(String wordTag, String wordCount) {
        this.wordCount = wordCount;
        this.wordTag = wordTag;
    }

    public String getWordTag() {
        return wordTag;
    }

    public String getWordCount() {
        return wordCount;
    }
}
