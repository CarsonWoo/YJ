package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.BasePresenter;
import com.example.carson.yjenglish.BaseView;
import com.example.carson.yjenglish.home.model.word.WordInfo;

/**
 * Created by 84594 on 2018/9/15.
 */

public interface WordInfoContract {
    interface Presenter extends BasePresenter {
        void getInfo(String token);
    }

    interface View extends BaseView<Presenter> {
        void showError(String msg);
        void setInfo(WordInfo wordInfo);
    }
}
