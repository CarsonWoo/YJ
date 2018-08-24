package com.example.carson.yjenglish.utils;

/**
 * Created by 84594 on 2018/8/18.
 * 文件夹信息
 */

public class FolderBean {
    private String dir;
    private String firstImgPath;//第一张图片的路径
    private String name;//文件夹的名字
    private int count;//图片数量

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int indexOf = this.dir.lastIndexOf("/") + 1;
        this.name = this.dir.substring(indexOf);
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
