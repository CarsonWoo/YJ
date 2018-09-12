package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.zone.model.PlanInfo;

/**
 * Created by 84594 on 2018/9/2.
 */

public interface PlanGetContract {
    interface Presenter extends BasePresenter {
        void setInfo(String type);
    }

    interface View extends BaseView<Presenter> {
        void showError(String msg);
        void showLoading();
        void hideLoading();
        boolean isActive();
        void getInfo(PlanInfo info);
    }
}
