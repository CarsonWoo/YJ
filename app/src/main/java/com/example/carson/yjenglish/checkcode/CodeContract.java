package com.example.carson.yjenglish.checkcode;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.checkcode.model.RegisterCodeBean;
import com.example.carson.yjenglish.utils.CommonInfo;

/**
 * Created by 84594 on 2018/7/31.
 */

public interface CodeContract {
    //验证code
    interface Presenter extends BasePresenter {
        void getResponse(RegisterCodeBean bean);
    }

    interface View extends BaseView<Presenter> {
        void showCodeLoading();
        void hideCodeLoading();
        //进行数据返回
        void getCode(CommonInfo info);
        void showCodeError(String msg);
    }
}
