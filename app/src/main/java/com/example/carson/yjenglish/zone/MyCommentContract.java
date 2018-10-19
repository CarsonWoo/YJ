package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.zone.model.MyCommentInfo;

/**
 * Created by 84594 on 2018/10/11.
 */

public interface MyCommentContract {
    interface Presenter extends BasePresenter {
        void getComments(String token);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void onSuccess(MyCommentInfo info);
    }
}
