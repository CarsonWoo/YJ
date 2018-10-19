package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.model.MyCommentInfo;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/10/11.
 */

public class MyCommentTask implements NetTask<String> {

    private static MyCommentTask INSTANCE;
    private Retrofit retrofit;
    private MyCommentTask() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static MyCommentTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyCommentTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String token, final LoadTasksCallback callback) {
        Subscription subscription;
        subscription = retrofit.create(ZoneService.class).getMyComments(token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyCommentInfo>() {
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
                    public void onNext(MyCommentInfo myCommentInfo) {
                        callback.onSuccess(myCommentInfo);
                    }
                });
        return subscription;
    }
}
