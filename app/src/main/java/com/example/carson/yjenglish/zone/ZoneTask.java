package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.model.ZoneInfo;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/9/19.
 */

public class ZoneTask implements NetTask<String> {
    private static ZoneTask INSTANCE = null;

    private Retrofit retrofit;

    private ZoneTask() {
        createRetrofit();
    }

    private void createRetrofit() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static ZoneTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ZoneTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String token, final LoadTasksCallback callback) {
        ZoneService service = retrofit.create(ZoneService.class);
        Subscription subscription = service.getUserInfo(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ZoneInfo>() {
                    @Override
                    public void onCompleted() {
                        callback.onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ZoneInfo zoneInfo) {
                        callback.onSuccess(zoneInfo);
                    }
                });
        return subscription;
    }
}
