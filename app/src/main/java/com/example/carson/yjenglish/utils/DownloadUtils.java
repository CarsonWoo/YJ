package com.example.carson.yjenglish.utils;

import android.support.annotation.NonNull;

import com.example.carson.yjenglish.DownloadListener;
import com.example.carson.yjenglish.DownloadService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/10/25.
 */

public class DownloadUtils {

    private static final String TAG = "DownloadUtils";

    private static final int DEFAULT_TIMEOUT = 15;

    private Retrofit retrofit;

    private DownloadListener downloadListener;

    private String baseUrl;

    private String downloadUrl;

    public DownloadUtils(String baseUrl, DownloadListener listener) {
        this.baseUrl = baseUrl;
        this.downloadListener = listener;

        DownloadInterceptor interceptor = new DownloadInterceptor(listener);

        OkHttpClient client = NetUtils.getInstance().getDownloadClient(interceptor);

        retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 下载
     */
    public void download(@NonNull String url, String filePath, Subscriber subscriber) {

        downloadListener.onStartDownload();

        // subscribeOn()改变调用它之前代码的线程
        // observeOn()改变调用它之后代码的线程
        retrofit.create(DownloadService.class).downloadFileRx(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())//用于计算任务
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        writeFile(inputStream, filePath);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 将输入流写入文件
     * @param inputStream 字节输入流
     * @param filePath 文件路径
     */
    private void writeFile(InputStream inputStream, String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;

            while ((len = inputStream.read(b)) != -1) {
                fos.write(b, 0, len);
            }

        } catch (IOException e) {
            downloadListener.onFail("IOException");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
