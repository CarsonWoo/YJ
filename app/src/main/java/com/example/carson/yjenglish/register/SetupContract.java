package com.example.carson.yjenglish.register;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.checkcode.model.RegisterCodeBean;
import com.example.carson.yjenglish.utils.CommonInfo;

/**
 * Created by 84594 on 2018/8/31.
 */

public interface SetupContract {
    interface Presenter extends BasePresenter {
        void getSetupResponse(RegisterCodeBean bean);
    }

    interface View extends BaseView<Presenter> {
        void getResponse(CommonInfo info);
        void showError(String msg);
    }
}
