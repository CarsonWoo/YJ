package com.example.carson.yjenglish.login;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.login.model.ForgetModel;
import com.example.carson.yjenglish.utils.CommonInfo;

/**
 * Created by 84594 on 2018/7/31.
 */

public interface ForgetContract {
    interface Presenter extends BasePresenter {
        void getCommonInfo(ForgetModel model);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void setCommonInfo(CommonInfo commonInfo);
    }
}
