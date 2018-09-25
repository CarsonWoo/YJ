package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.home.model.HomeInfo;
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
 * Created by 84594 on 2018/9/10.
 */

public class HomeInfoTask implements NetTask<String> {

    private Retrofit retrofit;
    private static HomeInfoTask INSTANCE = null;

    private HomeInfoTask() {
        createRetrofit();
    }

    private void createRetrofit() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static HomeInfoTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HomeInfoTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String token, final LoadTasksCallback callback) {
        HomeService service = retrofit.create(HomeService.class);
        Subscription subscription = service.getHomeInfo(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<HomeInfo>() {
                    @Override
                    public void onCompleted() {
                        callback.onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(HomeInfo homeInfo) {
                        callback.onSuccess(homeInfo);
                    }

                    @Override
                    public void onStart() {
                        callback.onStart();
                    }
                });
        return subscription;
    }
}
