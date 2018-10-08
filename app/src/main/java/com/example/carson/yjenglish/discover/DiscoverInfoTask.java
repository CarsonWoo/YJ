package com.example.carson.yjenglish.discover;

import com.example.carson.yjenglish.discover.model.DiscoverInfo;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/10/3.
 */

public class DiscoverInfoTask implements NetTask<String> {

    private static DiscoverInfoTask INSTANCE;
    private Retrofit retrofit;
    private DiscoverInfoTask() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static DiscoverInfoTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DiscoverInfoTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String data, final LoadTasksCallback callback) {
        DiscoverService service = retrofit.create(DiscoverService.class);
        Subscription subscription = service.getDiscoverInfo(data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<DiscoverInfo>() {
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
                    public void onNext(DiscoverInfo discoverInfo) {
                        callback.onSuccess(discoverInfo);
                    }
                });
        return subscription;
    }
}
