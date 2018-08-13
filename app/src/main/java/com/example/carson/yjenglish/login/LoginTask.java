package com.example.carson.yjenglish.login;

import com.example.carson.yjenglish.login.model.LoginInfo;
import com.example.carson.yjenglish.login.model.LoginModule;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/7/28.
 */

public class LoginTask implements NetTask<LoginModule> {
    private static LoginTask INSTANCE = null;
    //TODO 要将HOST替换
    private static final String HOST = "";
    private Retrofit retrofit;

    private LoginTask() {
        createRetrofit();
    }

    public static LoginTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoginTask();
        }
        return INSTANCE;
    }

    private void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Override
    public Subscription execute(LoginModule module, final LoadTasksCallback callback) {
        LoginService loginService = retrofit.create(LoginService.class);
        Observable<LoginInfo> mObservable;
        Subscription subscription;
        mObservable = loginService.getLoginResponse(module.getUsername(), module.getPassword());
        subscription = mObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginInfo>() {
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
                    public void onNext(LoginInfo loginInfo) {
                        callback.onSuccess(loginInfo);
                    }
                });
        return subscription;
    }
}
