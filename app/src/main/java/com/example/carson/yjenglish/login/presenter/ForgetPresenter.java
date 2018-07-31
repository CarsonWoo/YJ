package com.example.carson.yjenglish.login.presenter;

import com.example.carson.yjenglish.login.ForgetContract;
import com.example.carson.yjenglish.login.model.ForgetModel;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.uitls.CommonInfo;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/7/31.
 */

public class ForgetPresenter implements ForgetContract.Presenter, LoadTasksCallback<CommonInfo> {

    private NetTask netTask;
    private ForgetContract.View addTaskView;
    private CompositeSubscription mSubs;
    private Subscription subscription;

    public ForgetPresenter(NetTask netTask, ForgetContract.View addTaskView) {
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
    public void getCommonInfo(ForgetModel model) {
        subscription = netTask.execute(model, this);
        subscribe();
    }

    @Override
    public void onSuccess(CommonInfo info) {
        addTaskView.setCommonInfo(info);
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
