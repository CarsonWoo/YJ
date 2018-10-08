package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.home.model.AuthorInfo;
import com.example.carson.yjenglish.home.model.AuthorModel;
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
 * Created by 84594 on 2018/9/28.
 */

public class AuthorInfoTask implements NetTask<AuthorModel> {

    private static AuthorInfoTask INSTANCE;
    private Retrofit retrofit;

    private AuthorInfoTask() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static AuthorInfoTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthorInfoTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(AuthorModel data, final LoadTasksCallback callback) {
        HomeService service = retrofit.create(HomeService.class);
        Subscription subscription = service.getAuthorInfo(UserConfig.getToken(MyApplication.getContext()), data.getAuthor_id(),
                data.getPage(), data.getSize()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AuthorInfo>() {
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
                    public void onNext(AuthorInfo authorInfo) {
                        callback.onSuccess(authorInfo);
                    }
                });
        return subscription;
    }
}
