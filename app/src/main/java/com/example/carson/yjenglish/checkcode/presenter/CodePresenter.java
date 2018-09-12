package com.example.carson.yjenglish.checkcode.presenter;

import com.example.carson.yjenglish.checkcode.CodeContract;
import com.example.carson.yjenglish.checkcode.model.RegisterCodeBean;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.CommonInfo;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/7/31.
 */

public class CodePresenter implements CodeContract.Presenter, LoadTasksCallback<CommonInfo> {

    private NetTask netTask;
    private CodeContract.View addTaskView;
    private CompositeSubscription mSubs;
    private Subscription subscription;

    public CodePresenter(NetTask netTask, CodeContract.View addTaskView) {
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
    public void onSuccess(CommonInfo info) {
        addTaskView.getCode(info);
    }

    @Override
    public void onStart() {
        addTaskView.showCodeLoading();
    }

    @Override
    public void onFailed(String msg) {
        addTaskView.showCodeError(msg);
        addTaskView.hideCodeLoading();
    }

    @Override
    public void onFinish() {
        addTaskView.hideCodeLoading();
    }

    @Override
    public void getResponse(RegisterCodeBean bean) {
        subscription = netTask.execute(bean, this);
        subscribe();
    }
}
