package com.example.carson.yjenglish.login;

import com.example.carson.yjenglish.login.model.ForgetModel;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/7/31.
 */

public class ForgetTask implements NetTask<ForgetModel> {

    private static ForgetTask INSTANCE = null;
    private static final String HOST = UserConfig.HOST;
    private Retrofit retrofit;

    private ForgetTask() {
        createRetrofit();
    }

    public static ForgetTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ForgetTask();
        }
        return INSTANCE;
    }

    private void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .client(NetUtils.getInstance().getClientInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Override
    public Subscription execute(ForgetModel data, final LoadTasksCallback callback) {
        ForgetService forgetService = retrofit.create(ForgetService.class);
        Subscription subscription = forgetService.getResponse(data.getForget_password_token(), data.getPassword())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonInfo>() {
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
                    public void onNext(CommonInfo info) {
                        callback.onSuccess(info);
                    }
                });
        return subscription;
    }
}
