package com.example.carson.yjenglish.register;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.model.RegisterModel;

/**
 * Created by 84594 on 2018/7/29.
 */

public interface RegisterContract {
    interface Presenter extends BasePresenter {
        void getRegisterResponse(RegisterModel model);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void showError(String msg);
        void getResponse(RegisterInfo info);
        void hideLoading();
    }
}
