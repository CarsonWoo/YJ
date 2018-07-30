package com.example.carson.yjenglish.register;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.model.RegisterModel;

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
    private static final String HOST = "";

    private RegisterTask() {
        createRetrofit();
    }

    private void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
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
        Subscription subscription = registerService.getResponse(model)
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
