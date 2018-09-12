package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.home.model.HomeInfo;

/**
 * Created by 84594 on 2018/9/10.
 */

public interface HomeInfoContract {

    interface Presenter extends BasePresenter {
        void getInfo(String token);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void setInfo(HomeInfo info);
        boolean isActive();
    }

}
