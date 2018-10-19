package com.example.carson.yjenglish.msg;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.msg.model.FavoursInfo;

/**
 * Created by 84594 on 2018/10/18.
 */

public interface GetFavoursContract {
    interface Presenter extends BasePresenter {
        void getInfo(String token);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void onSuccess(FavoursInfo info);
        boolean isViewAdded();
    }
}
