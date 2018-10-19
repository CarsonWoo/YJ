package com.example.carson.yjenglish.zone.presenter;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.zone.MyFavourContract;
import com.example.carson.yjenglish.zone.model.MyFavourInfo;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/10/12.
 */

public class MyFavourPresenter implements MyFavourContract.Presenter, LoadTasksCallback<MyFavourInfo> {

    private NetTask netTask;
    private MyFavourContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public MyFavourPresenter(NetTask netTask, MyFavourContract.View view) {
        this.netTask = netTask;
        this.addTaskView = view;
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
    public void onSuccess(MyFavourInfo info) {
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
    public void getMyFavours(String token) {
        subscription = netTask.execute(token, this);
        subscribe();
    }
}
