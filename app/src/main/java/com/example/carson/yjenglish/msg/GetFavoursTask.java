package com.example.carson.yjenglish.msg;

import com.example.carson.yjenglish.msg.model.FavoursInfo;
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
 * Created by 84594 on 2018/10/18.
 */

public class GetFavoursTask implements NetTask<String> {

    private static GetFavoursTask INSTANCE;
    private Retrofit retrofit;

    private GetFavoursTask() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static GetFavoursTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GetFavoursTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String data, final LoadTasksCallback callback) {
        Subscription subscription;
        subscription = retrofit.create(MsgService.class).getFavoursInfo(data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<FavoursInfo>() {
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
                    public void onNext(FavoursInfo favoursInfo) {
                        callback.onSuccess(favoursInfo);
                    }
                });
        return subscription;
    }
}
