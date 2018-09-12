package com.example.carson.yjenglish.register;

import android.util.Log;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.net.NullOnEmptyConverterFactory;
import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.model.RegisterModel;
import com.example.carson.yjenglish.utils.AddCookiesInterceptor;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.SaveCookiesInterceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/7/29.
 */

public class RegisterTask implements NetTask<RegisterModel> {

    private static RegisterTask INSTANCE = null;
    private Retrofit retrofit;
    private static final String HOST = "http://123.207.85.37:8080/";

    private RegisterTask() {
        createRetrofit();
    }

    private void createRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                String text;
                try {
                    text = URLDecoder.decode(message, "utf-8");
                    Log.e("======", text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("======", e.getMessage());
                }
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(new SaveCookiesInterceptor());
//        builder.addInterceptor(new AddCookiesInterceptor());
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .client(builder.build())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static RegisterTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RegisterTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(RegisterModel model, final LoadTasksCallback callback) {
        RegisterService registerService = retrofit.create(RegisterService.class);
        Subscription subscription = registerService.getResponse(model.getToken(), model.getPhone())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegisterInfo>() {
                    @Override
                    public void onStart() {
                        callback.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        callback.onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(RegisterInfo registerInfo) {
                        callback.onSuccess(registerInfo);
                    }
                });
        return subscription;
    }
}
