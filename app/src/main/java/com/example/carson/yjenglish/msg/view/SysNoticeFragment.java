package com.example.carson.yjenglish.msg.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.R;

/**
 * Created by 84594 on 2018/8/6.
 */

public class SysNoticeFragment extends Fragment {

    private static final String TAG = "NoticeFragment";
    private static SysNoticeFragment INSTANCE = null;

    public SysNoticeFragment(){}
    public static SysNoticeFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SysNoticeFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }
}
