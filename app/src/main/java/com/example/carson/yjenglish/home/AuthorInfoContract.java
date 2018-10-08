package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.home.model.AuthorInfo;
import com.example.carson.yjenglish.home.model.AuthorModel;

/**
 * Created by 84594 on 2018/9/28.
 */

public interface AuthorInfoContract {
    interface Presenter extends BasePresenter {
        void getAuthorInfo(AuthorModel model);
    }
    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void onSuccess(AuthorInfo info);
    }
}
