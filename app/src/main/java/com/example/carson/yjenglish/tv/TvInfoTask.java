package com.example.carson.yjenglish.tv;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.tv.model.TVInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/9/30.
 */

public class TvInfoTask implements NetTask<String> {

    private static TvInfoTask INSTANCE;
    private Retrofit retrofit;
    private TvInfoTask() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static TvInfoTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TvInfoTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String data, final LoadTasksCallback callback) {
        TVService service = retrofit.create(TVService.class);
        Subscription subscription = service.getTvInfo(data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<TVInfo>() {
                    @Override
                    public void onCompleted() {
                        callback.onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(TVInfo tvInfo) {
                        callback.onSuccess(tvInfo);
                    }

                    @Override
                    public void onStart() {
                        callback.onStart();
                    }
                });
        return subscription;
    }
}
