package com.example.carson.yjenglish.zone.viewbinder;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.zone.model.ZoneInfo;

/**
 * Created by 84594 on 2018/9/19.
 */

public interface ZoneContract {
    interface Presenter extends BasePresenter {
        void getUserInfo(String token);
    }

    interface View extends BaseView<Presenter> {
        void onSuccess(ZoneInfo Info);
        void onFailed(String msg);
    }
}
