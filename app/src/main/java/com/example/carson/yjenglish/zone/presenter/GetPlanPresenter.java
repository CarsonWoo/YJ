package com.example.carson.yjenglish.zone.presenter;

import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.zone.PlanGetContract;
import com.example.carson.yjenglish.zone.model.PlanInfo;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 84594 on 2018/9/2.
 */

public class GetPlanPresenter implements PlanGetContract.Presenter, LoadTasksCallback<PlanInfo> {

    private NetTask netTask;
    private PlanGetContract.View addTaskView;
    private Subscription subscription;
    private CompositeSubscription mSubs;

    public GetPlanPresenter(NetTask netTask, PlanGetContract.View addTaskView) {
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
    public void onSuccess(PlanInfo info) {
        if (addTaskView.isActive()) {
            addTaskView.getInfo(info);
        }
    }

    @Override
    public void onStart() {
        if (addTaskView.isActive()) {
            addTaskView.showLoading();
        }
    }

    @Override
    public void onFailed(String msg) {
        if (addTaskView.isActive()) {
            addTaskView.showError(msg);
            addTaskView.hideLoading();
        }
    }

    @Override
    public void onFinish() {
        if (addTaskView.isActive()) {
            addTaskView.hideLoading();
        }
    }

    @Override
    public void setInfo(String type) {
        subscription = netTask.execute(type, this);
        subscribe();
    }
}
