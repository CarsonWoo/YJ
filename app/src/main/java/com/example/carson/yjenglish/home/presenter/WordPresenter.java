package com.example.carson.yjenglish.home.presenter;

import com.example.carson.yjenglish.home.WordInfoContract;
import com.example.carson.yjenglish.home.model.word.WordInfo;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/9/15.
 */

public class WordPresenter implements WordInfoContract.Presenter, LoadTasksCallback<WordInfo> {

    private NetTask netTask;
    private WordInfoContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public WordPresenter(NetTask netTask, WordInfoContract.View addTaskView) {
        this.netTask = netTask;
        this.addTaskView = addTaskView;
        mSubs = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        if (subscription != null) {
            mSubs.add(subscription);
        }
    }

    @Override
    public void unsubscribe() {
        if (mSubs != null && mSubs.hasSubscriptions()) {
            mSubs.unsubscribe();
        }
    }

    @Override
    public void onSuccess(WordInfo wordInfo) {
        addTaskView.setInfo(wordInfo);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFailed(String msg) {
        addTaskView.showError(msg);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void getInfo(String token) {
        netTask.execute(token, this);
    }
}
