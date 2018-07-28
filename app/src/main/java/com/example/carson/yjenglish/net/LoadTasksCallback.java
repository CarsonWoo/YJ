package com.example.carson.yjenglish.net;

/**
 * Created by 84594 on 2018/7/28.
 */

public interface LoadTasksCallback<T> {
    void onSuccess(T t);
    void onStart();
    void onFailed(String msg);
    void onFinish();
}
