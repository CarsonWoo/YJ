package com.example.carson.yjenglish;

import android.support.v4.app.Fragment;

/**
 * Created by 84594 on 2018/8/1.
 * 进行懒加载的Fragment 可参考使用
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 关键 这个方法根据当前Fragment是否对用户可见返回相应的布尔值
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
