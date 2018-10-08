package com.example.carson.yjenglish.tv.presenter;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.tv.TVInfoContract;
import com.example.carson.yjenglish.tv.model.TVInfo;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/9/30.
 */

public class TVInfoPresenter implements TVInfoContract.Presenter, LoadTasksCallback<TVInfo> {

    private NetTask netTask;
    private TVInfoContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public TVInfoPresenter(NetTask netTask, TVInfoContract.View addTaskView) {
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
    public void onSuccess(TVInfo info) {
        addTaskView.onSuccess(info);
    }

    @Override
    public void onStart() {
        addTaskView.showLoading();
    }

    @Override
    public void onFailed(String msg) {
        addTaskView.showError(msg);
        addTaskView.hideLoading();
    }

    @Override
    public void onFinish() {
        addTaskView.hideLoading();
    }

    @Override
    public void getTvInfo(String token) {
        subscription = netTask.execute(token, this);
        subscribe();
    }
}
