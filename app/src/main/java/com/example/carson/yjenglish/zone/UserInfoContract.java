package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.zone.model.UserBasicModel;

/**
 * Created by 84594 on 2018/9/20.
 */

public interface UserInfoContract {
    interface Presenter extends BasePresenter {
        void getUserInfo(UserBasicModel info);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void updateUserInfo(CommonInfo info);
    }
}
