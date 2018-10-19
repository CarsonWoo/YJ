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
import com.example.carson.yjenglish.msg.model.NoticeMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 84594 on 2018/8/6.
 */

public class SysNoticeFragment extends Fragment {

    private static final String TAG = "NoticeFragment";
    private static SysNoticeFragment INSTANCE = null;

    private PullToRefreshRecyclerView recyclerView;
    private List<NoticeMsg> mNotices;
    private MsgAdapters.SysAdapter mAdapter;

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
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);

        mNotices = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            NoticeMsg notice = new NoticeMsg();
//            notice.setUsername("系统君");
//            notice.setContent("恭喜您完成了打卡任务");
//            notice.setDate("刚刚");
//            mNotices.add(notice);
//        }

        mAdapter = new MsgAdapters.SysAdapter(getContext(), mNotices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        View emptyView = View.inflate(getContext(), R.layout.layout_error, null);
        TextView errorMsg = emptyView.findViewById(R.id.error_text);
        errorMsg.setText("暂时没有系统通知噢...");
        ImageView errorImg = emptyView.findViewById(R.id.error_img);
        errorImg.setImageResource(R.mipmap.bg_plan_box);
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
