package com.example.carson.yjenglish.home.model;

/**
 * Created by 84594 on 2018/9/28.
 */

public class AuthorModel {
    private String author_id;
    private String page;
    private String size;

    public AuthorModel(String author_id, String page, String size) {
        this.author_id = author_id;
        this.page = page;
        this.size = size;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public String getPage() {
        return page;
    }

    public String getSize() {
        return size;
    }
}
