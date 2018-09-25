package com.example.carson.yjenglish.zone.presenter;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.zone.AdviceContract;
import com.example.carson.yjenglish.zone.model.AdviceModel;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/9/24.
 */

public class AdvicePresenter implements AdviceContract.Presenter, LoadTasksCallback<CommonInfo> {

    private NetTask netTask;
    private AdviceContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public AdvicePresenter(NetTask netTask, AdviceContract.View view) {
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
    public void sendAdvice(AdviceModel model) {
        subscription = netTask.execute(model, this);
        subscribe();
    }

    @Override
    public void onSuccess(CommonInfo info) {
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
