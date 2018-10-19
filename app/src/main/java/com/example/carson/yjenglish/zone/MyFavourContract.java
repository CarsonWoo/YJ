package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.zone.model.MyFavourInfo;

/**
 * Created by 84594 on 2018/10/12.
 */

public interface MyFavourContract {

    interface Presenter extends BasePresenter {
        void getMyFavours(String token);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void onSuccess(MyFavourInfo info);
    }

}
