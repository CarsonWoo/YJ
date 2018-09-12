package com.example.carson.yjenglish.home.presenter;

import com.example.carson.yjenglish.home.HomeInfoContract;
import com.example.carson.yjenglish.home.model.HomeInfo;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/9/10.
 */

public class HomeInfoPresenter implements HomeInfoContract.Presenter, LoadTasksCallback<HomeInfo> {

    private NetTask netTask;
    private HomeInfoContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public HomeInfoPresenter(NetTask task, HomeInfoContract.View addTaskView) {
        this.netTask = task;
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
    public void getInfo(String token) {
        subscription = netTask.execute(token, this);
        subscribe();
    }

    @Override
    public void onSuccess(HomeInfo info) {
        if (addTaskView.isActive()) {
            addTaskView.setInfo(info);
        }
    }

    @Override
    public void onStart() {
        if (addTaskView.isActive()) {
            addTaskView.showLoading();
        }
    }

    @Override
    public void onFailed(String msg) {
        if (addTaskView.isActive()) {
            addTaskView.showError(msg);
            addTaskView.hideLoading();
        }
    }

    @Override
    public void onFinish() {
        if (addTaskView.isActive()) {
            addTaskView.hideLoading();
        }
    }
}
