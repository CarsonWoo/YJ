package com.example.carson.yjenglish.register.presenter;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.register.RegisterContract;
import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.model.RegisterModel;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/7/29.
 */

public class RegisterPresenter implements RegisterContract.Presenter, LoadTasksCallback<RegisterInfo> {
    private NetTask netTask;
    private RegisterContract.View addTaskView;
    private CompositeSubscription mSubscriptions;
    private Subscription subscription;

    public RegisterPresenter(NetTask netTask, RegisterContract.View addTaskView) {
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
    public void onSuccess(RegisterInfo info) {
        addTaskView.getResponse(info);
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
    public void getRegisterResponse(RegisterModel model) {
        subscription = netTask.execute(model, this);
        subscribe();
    }
}
