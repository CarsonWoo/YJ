package com.example.carson.yjenglish.utils;

import com.example.carson.yjenglish.DownloadListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by 84594 on 2018/10/25.
 */

public class DownloadInterceptor implements Interceptor {

    private DownloadListener downloadListener;

    public DownloadInterceptor(DownloadListener listener) {
        this.downloadListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new DownloadResponseBody(response.body(), downloadListener)
        ).build();
    }
}
