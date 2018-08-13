package com.example.carson.yjenglish.checkcode;

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
 * Created by 84594 on 2018/7/31.
 */

public class CodeTask implements NetTask<String> {

    private final int TYPE_REGISTER = 1;
    private final int TYPE_FORGET = 0;

    private static CodeTask INSTANCE = null;

    private static final String HOST = "";
    private Retrofit retrofit;
    private int type;

    public static CodeTask getInstance(int type) {
        if (INSTANCE == null) {
            INSTANCE = new CodeTask(type);
        }
        return INSTANCE;
    }

    private CodeTask(int type) {
        this.type = type;
        createRetrofit();
    }

    private void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Override
    public Subscription execute(String data, final LoadTasksCallback callback) {
        CodeService codeService = retrofit.create(CodeService.class);
        Observable<Integer> mObservable;
        Subscription subscription;
        if (type == TYPE_FORGET) {
            mObservable = codeService.getLoginCode(data);
        } else {
            mObservable = codeService.getRegisterCode(data);
        }
        subscription = mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
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
                    public void onNext(Integer integer) {
                        callback.onSuccess(integer);
                    }
                });
        return subscription;
    }
}
