package com.example.carson.yjenglish.home.presenter;

import com.example.carson.yjenglish.home.AuthorInfoContract;
import com.example.carson.yjenglish.home.model.AuthorInfo;
import com.example.carson.yjenglish.home.model.AuthorModel;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/9/28.
 */

public class AuthorInfoPresenter implements AuthorInfoContract.Presenter, LoadTasksCallback<AuthorInfo> {

    private NetTask netTask;
    private AuthorInfoContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public AuthorInfoPresenter(NetTask netTask, AuthorInfoContract.View addTaskView) {
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
    public void onSuccess(AuthorInfo info) {
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
    public void getAuthorInfo(AuthorModel model) {
        subscription = netTask.execute(model, this);
        subscribe();
    }
}
