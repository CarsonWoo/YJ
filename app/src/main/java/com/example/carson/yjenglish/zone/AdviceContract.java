package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.zone.model.AdviceModel;

/**
 * Created by 84594 on 2018/9/24.
 */

public interface AdviceContract {
    interface Presenter extends BasePresenter {
        void sendAdvice(AdviceModel model);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String msg);
        void onSuccess(CommonInfo info);
    }
}
