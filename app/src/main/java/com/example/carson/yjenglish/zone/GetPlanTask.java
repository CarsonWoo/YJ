package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.model.PlanInfo;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/9/2.
 */

public class GetPlanTask implements NetTask<String> {
    private static GetPlanTask INSTANCE = null;
    private Retrofit retrofit;
    private static final String HOST = UserConfig.HOST;
    private GetPlanTask() {
        createRetrofit();
    }

    private void createRetrofit() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(HOST);
    }

    public static GetPlanTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GetPlanTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String data, final LoadTasksCallback callback) {
        PlanService service = retrofit.create(PlanService.class);
        Subscription subscription = service.getPlans(data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<PlanInfo>() {
                    @Override
                    public void onCompleted() {
                        callback.onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(PlanInfo planInfo) {
                        callback.onSuccess(planInfo);
                    }

                    @Override
                    public void onStart() {
                        callback.onStart();
                    }
                });
        return subscription;
    }
}
