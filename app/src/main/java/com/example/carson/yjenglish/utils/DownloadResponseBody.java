package com.example.carson.yjenglish.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.carson.yjenglish.DownloadListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by 84594 on 2018/10/25.
 */

public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private DownloadListener downloadListener;

    //okio的输入流 当作inputstream使用
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, DownloadListener listener) {
        this.responseBody = responseBody;
        this.downloadListener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalByteRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long byteRead = super.read(sink, byteCount);
                // read()方法 返回下载的字节数,或者返回下载完则返回-1
                totalByteRead += byteRead != -1 ? byteRead : 0;
                Log.e("download", "read :" + (int) (totalByteRead * 100 / responseBody.contentLength()));
                if (null != downloadListener) {
                    if (byteRead != -1) {
                        downloadListener.onProgress((int) (totalByteRead * 100 / responseBody.contentLength()));
                    }
                }
                return byteRead;
            }
        };
    }
}
