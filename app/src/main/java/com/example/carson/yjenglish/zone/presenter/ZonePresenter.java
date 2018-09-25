package com.example.carson.yjenglish.zone.presenter;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.zone.model.ZoneInfo;
import com.example.carson.yjenglish.zone.viewbinder.ZoneContract;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/9/19.
 */

public class ZonePresenter implements ZoneContract.Presenter, LoadTasksCallback<ZoneInfo> {
    private NetTask netTask;
    private ZoneContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public ZonePresenter(NetTask netTask, ZoneContract.View addTaskView) {
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
    public void onSuccess(ZoneInfo info) {
        addTaskView.onSuccess(info);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFailed(String msg) {
        addTaskView.onFailed(msg);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void getUserInfo(String token) {
        subscription = netTask.execute(token, this);
        subscribe();
    }
}
