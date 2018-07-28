package com.example.carson.yjenglish.login;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.login.model.LoginInfo;
import com.example.carson.yjenglish.login.model.LoginModule;

/**
 * Created by 84594 on 2018/7/28.
 * view与presenter的契约接口
 */

public interface LoginContract {
    interface Presenter extends BasePresenter {
        void getLoginInfo(LoginModule module);
    }

    interface View extends BaseView<Presenter> {
        void setLoginInfo(LoginInfo info);
        void showLoading();
        void hideLoading();
        void showError(String msg);
    }
}
