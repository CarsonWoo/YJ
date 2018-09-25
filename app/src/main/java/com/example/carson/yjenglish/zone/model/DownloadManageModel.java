package com.example.carson.yjenglish.zone.model;

/**
 * Created by 84594 on 2018/9/25.
 */

public class DownloadManageModel {
    private String fileName;
    private int size;
    private int curSize;
    private boolean isFailed;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSize() {
        return size;
    }

    public int getCurSize() {
        return curSize;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setCurSize(int curSize) {
        this.curSize = curSize;
    }
}
