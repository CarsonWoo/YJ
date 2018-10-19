package com.example.carson.yjenglish.zone.presenter;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.zone.MyCommentContract;
import com.example.carson.yjenglish.zone.model.MyCommentInfo;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/10/11.
 */

public class MyCommentPresenter implements MyCommentContract.Presenter, LoadTasksCallback<MyCommentInfo> {

    private NetTask netTask;
    private Subscription subscription;
    private CompositeSubscription mSubs;
    private MyCommentContract.View addTaskView;

    public MyCommentPresenter(NetTask netTask, MyCommentContract.View addTaskView) {
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
    public void getComments(String token) {
        subscription = netTask.execute(token, this);
        subscribe();
    }

    @Override
    public void onSuccess(MyCommentInfo info) {
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
