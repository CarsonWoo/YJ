package com.example.carson.yjenglish.discover.presenter;

import com.example.carson.yjenglish.discover.DiscoverInfoContract;
import com.example.carson.yjenglish.discover.model.DiscoverInfo;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/10/3.
 */

public class DiscoverInfoPresenter implements DiscoverInfoContract.Presenter, LoadTasksCallback<DiscoverInfo> {

    private NetTask netTask;
    private DiscoverInfoContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public DiscoverInfoPresenter(NetTask netTask, DiscoverInfoContract.View addTaskView) {
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
    public void getInfo(String token) {
        subscription = netTask.execute(token, this);
        subscribe();
    }

    @Override
    public void onSuccess(DiscoverInfo info) {
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
}
