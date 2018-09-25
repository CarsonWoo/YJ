package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.home.model.word.WordInfo;
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
 * Created by 84594 on 2018/9/15.
 */

public class WordInfoTask implements NetTask<String> {

    private Retrofit retrofit;
    private static WordInfoTask INSTANCE = null;
    private WordInfoTask() {
        createRetrofit();
    }

    private void createRetrofit() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
    }

    public static WordInfoTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WordInfoTask();
        }
        return INSTANCE;
    }

    @Override
    public Subscription execute(String token, final LoadTasksCallback callback) {
        WordService service = retrofit.create(WordService.class);
        Subscription subscription = service.startLearning(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<WordInfo>() {
                    @Override
                    public void onCompleted() {
                        callback.onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(WordInfo wordInfo) {
                        callback.onSuccess(wordInfo);
                    }

                    @Override
                    public void onStart() {
                        callback.onStart();
                    }
                });
        return subscription;
    }
}
