package com.example.carson.yjenglish.zone.presenter;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.zone.UserInfoContract;
import com.example.carson.yjenglish.zone.model.UserBasicModel;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/9/20.
 */

public class UserInfoPresenter implements UserInfoContract.Presenter, LoadTasksCallback<CommonInfo> {

    private NetTask netTask;
    private UserInfoContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public UserInfoPresenter(NetTask netTask, UserInfoContract.View addTaskView) {
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
    public void getUserInfo(UserBasicModel info) {
        subscription = netTask.execute(info, this);
        subscribe();
    }

    @Override
    public void onSuccess(CommonInfo info) {
        addTaskView.updateUserInfo(info);
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
