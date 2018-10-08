package com.example.carson.yjenglish.discover;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.discover.model.DiscoverInfo;

/**
 * Created by 84594 on 2018/10/3.
 */

public interface DiscoverInfoContract {
    interface Presenter extends BasePresenter {
        void getInfo(String token);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void onSuccess(DiscoverInfo info);
    }

}
