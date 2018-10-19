package com.example.carson.yjenglish.msg.presenter;

import com.example.carson.yjenglish.msg.GetFavoursContract;
import com.example.carson.yjenglish.msg.model.FavoursInfo;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/10/18.
 */

public class GetFavoursPresenter implements GetFavoursContract.Presenter, LoadTasksCallback<FavoursInfo> {

    private NetTask netTask;
    private GetFavoursContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public GetFavoursPresenter(NetTask netTask, GetFavoursContract.View addTaskView) {
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
    public void onSuccess(FavoursInfo info) {
        if (addTaskView.isViewAdded()) {
            addTaskView.onSuccess(info);
        }
    }

    @Override
    public void onStart() {
        if (addTaskView.isViewAdded()) {
            addTaskView.showLoading();
        }
    }

    @Override
    public void onFailed(String msg) {
        if (addTaskView.isViewAdded()) {
            addTaskView.showError(msg);
            addTaskView.hideLoading();
        }
    }

    @Override
    public void onFinish() {
        if (addTaskView.isViewAdded()) {
            addTaskView.hideLoading();
        }
    }
}
