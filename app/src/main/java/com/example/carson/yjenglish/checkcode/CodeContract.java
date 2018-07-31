package com.example.carson.yjenglish.checkcode;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;

/**
 * Created by 84594 on 2018/7/31.
 */

public interface CodeContract {
    interface Presenter extends BasePresenter {
        void getResponse(String phone);
    }

    interface View extends BaseView<Presenter> {
        void showCodeLoading();
        void hideCodeLoading();
        //进行数据返回
        void getCode(int code);
        void showCodeError(String msg);
    }
}
