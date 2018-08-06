package com.example.carson.yjenglish.msg.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.MsgAdapters;
import com.example.carson.yjenglish.msg.model.LikeMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 84594 on 2018/8/6.
 */

public class LikeFragment extends Fragment {

    private static final String TAG = "LikeFragment";
    private static LikeFragment INSTANCE = null;

    private PullToRefreshRecyclerView recyclerView;
    private List<LikeMsg> mLikes;
    private MsgAdapters.LikeAdapter mAdapter;

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

        mLikes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LikeMsg like = new LikeMsg();
            like.setUsername("帅的被人砍");
            like.setContent("帅哥的文章就是不一样");
            like.setDate("刚刚");
            like.setPortraitUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg");
            mLikes.add(like);
        }

        mAdapter = new MsgAdapters.LikeAdapter(getContext(), mLikes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        View emptyView = View.inflate(getContext(), R.layout.layout_error, null);
        TextView errorMsg = emptyView.findViewById(R.id.error_text);
        errorMsg.setText("暂时没有数据噢...");
        ImageView errorImg = emptyView.findViewById(R.id.error_img);
        errorImg.setImageResource(R.drawable.ic_warning);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setEmptyView(emptyView);

        recyclerView.setLoadingMoreEnabled(false);

        recyclerView.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setRefreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {

            }
        });
    }
}
