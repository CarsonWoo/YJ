package com.example.carson.yjenglish.tv;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.tv.model.TVInfo;

/**
 * Created by 84594 on 2018/9/30.
 */

public interface TVInfoContract {
    interface Presenter extends BasePresenter {
        void getTvInfo(String token);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void onSuccess(TVInfo info);
    }
}
