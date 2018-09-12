package com.example.carson.yjenglish.register.presenter;

import com.example.carson.yjenglish.checkcode.model.RegisterCodeBean;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.register.SetupContract;
import com.example.carson.yjenglish.utils.CommonInfo;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/8/31.
 */

public class SetupPresenter implements SetupContract.Presenter, LoadTasksCallback<CommonInfo> {

    private NetTask netTask;
    private SetupContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public SetupPresenter(NetTask netTask, SetupContract.View addTaskView) {
        this.addTaskView = addTaskView;
        this.netTask = netTask;
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
    public void getSetupResponse(RegisterCodeBean bean) {
        subscription = netTask.execute(bean, this);
        subscribe();
    }

    @Override
    public void onSuccess(CommonInfo info) {
        addTaskView.getResponse(info);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFailed(String msg) {
        addTaskView.showError(msg);
    }

    @Override
    public void onFinish() {

    }
}
