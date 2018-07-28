package com.example.carson.yjenglish.login.presenter;

import com.example.carson.yjenglish.login.LoginContract;
import com.example.carson.yjenglish.login.model.LoginInfo;
import com.example.carson.yjenglish.login.model.LoginModule;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/7/28.
 */

public class LoginPresenter implements LoginContract.Presenter, LoadTasksCallback<LoginInfo> {

    private NetTask netTask;
    private LoginContract.View addTaskView;
    private CompositeSubscription mSubscriptions;
    private Subscription subscription;
    public LoginPresenter(LoginContract.View addTaskView, NetTask netTask) {
        this.netTask = netTask;
        this.addTaskView = addTaskView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        if (subscription != null) {
            mSubscriptions.add(subscription);
        }
    }

    @Override
    public void unsubscribe() {
        if (mSubscriptions != null && mSubscriptions.hasSubscriptions()) {
            mSubscriptions.unsubscribe();
        }
    }

    @Override
    public void onSuccess(LoginInfo loginInfo) {
        addTaskView.setLoginInfo(loginInfo);
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
    public void getLoginInfo(LoginModule module) {
        subscription = netTask.execute(module, this);
        subscribe();
    }
}
