package com.example.carson.yjenglish.msg.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidkun.PullToRefreshRecyclerView;
import com.example.carson.yjenglish.R;

/**
 * Created by 84594 on 2018/8/6.
 */

public class LikeFragment extends Fragment {

    private static final String TAG = "LikeFragment";
    private static LikeFragment INSTANCE = null;

    private PullToRefreshRecyclerView recyclerView;

    public LikeFragment() {}
    public static LikeFragment getInstance () {
        if (INSTANCE == null) {
            INSTANCE = new LikeFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);

    }
}
