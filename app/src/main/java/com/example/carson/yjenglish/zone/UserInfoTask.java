package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.model.UserBasicModel;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/9/20.
 */

public class UserInfoTask implements NetTask<UserBasicModel>  {

    private static UserInfoTask INSTANCE;
    private Retrofit retrofit;

    private UserInfoTask() {
        createRetrofit();
    }

    private void createRetrofit() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static UserInfoTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserInfoTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(UserBasicModel data, final LoadTasksCallback callback) {
        ZoneService service = retrofit.create(ZoneService.class);
        Subscription subscription = service.changeUserBasicInfo(data.getToken(),
                data.getGender(), data.getUsername(), data.getPersonality_signature())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonInfo>() {
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
