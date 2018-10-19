package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.model.MyFavourInfo;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/10/12.
 */

public class MyFavourTask implements NetTask<String> {

    private static MyFavourTask INSTANCE;

    private Retrofit retrofit;

    private MyFavourTask() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static MyFavourTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyFavourTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String data, final LoadTasksCallback callback) {
        Subscription subscription;
        subscription = retrofit.create(ZoneService.class).getMyFavours(data)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyFavourInfo>() {
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
                        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
                            callback.onFailed(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(MyFavourInfo myFavourInfo) {
                        callback.onSuccess(myFavourInfo);
                    }
                });
        return subscription;
    }
}
