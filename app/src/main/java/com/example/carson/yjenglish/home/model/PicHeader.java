package com.example.carson.yjenglish.home.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by 84594 on 2018/8/1.
 */

public class PicHeader extends DataSupport {

    @Column(defaultValue = "30000")
    private String number;

    @Column(defaultValue = "1")
    private int header_id;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getHeader_id() {
        return header_id;
    }

    public void setHeader_id(int header_id) {
        this.header_id = header_id;
    }
}
