package com.example.carson.yjenglish.net;

import rx.Subscription;

/**
 * Created by 84594 on 2018/7/28.
 */

public interface NetTask<T> {
    Subscription execute(T data, LoadTasksCallback callback);
}
