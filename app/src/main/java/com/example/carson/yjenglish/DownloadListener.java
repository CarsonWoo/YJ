package com.example.carson.yjenglish;

/**
 * Created by 84594 on 2018/10/25.
 */

public interface DownloadListener {

    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);
}
