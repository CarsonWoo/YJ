package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.model.AdviceModel;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/9/24.
 */

public class AdviceTask implements NetTask<AdviceModel> {

    private static AdviceTask INSTANCE;
    private Retrofit retrofit;

    private AdviceTask() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static AdviceTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AdviceTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(AdviceModel model, final LoadTasksCallback callback) {
        Subscription subscription = retrofit.create(ZoneService.class).sendAdvice(UserConfig.getToken(MyApplication.getContext()),
                model.getAdvice(), model.getLevel()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CommonInfo>() {
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

                    @Override
                    public void onStart() {
                        callback.onStart();
                    }
                });
        return subscription;
    }
}
