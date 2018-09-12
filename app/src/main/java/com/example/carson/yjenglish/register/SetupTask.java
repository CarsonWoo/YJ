package com.example.carson.yjenglish.register;

import com.example.carson.yjenglish.checkcode.model.RegisterCodeBean;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.net.NullOnEmptyConverterFactory;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/8/31.
 */

public class SetupTask implements NetTask<RegisterCodeBean> {

    private static SetupTask INSTANCE = null;
    private static final String HOST = UserConfig.HOST;
    private Retrofit retrofit;

    private SetupTask() {
        createRetrofit();
    }

    public static SetupTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SetupTask();
        }
        return INSTANCE;
    }

    private void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .client(NetUtils.getInstance().getClientInstance())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Override
    public Subscription execute(RegisterCodeBean bean, final LoadTasksCallback callback) {
        SetupService service = retrofit.create(SetupService.class);
        Subscription subscription = service.getResponse(bean.getRegister_token(), bean.getPhone_code()).subscribeOn(Schedulers.io())
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
