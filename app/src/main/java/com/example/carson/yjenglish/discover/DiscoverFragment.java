package com.example.carson.yjenglish.discover;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.DiscoverAdapters;
import com.example.carson.yjenglish.adapter.MsgAdapters;
import com.example.carson.yjenglish.msg.model.CommentMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    private PullToRefreshRecyclerView rvAty, rvGame;
    private ImageView sentenceView;

    private DiscoverAdapters.AtyAdapter mAdapter;
    private List<String> mList;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        sentenceView = view.findViewById(R.id.daily_sentence_img);
        rvAty = view.findViewById(R.id.rv_aty);
        rvGame = view.findViewById(R.id.rv_game);

        rvAty.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGame.setLayoutManager(new LinearLayoutManager(getContext()));

        rvAty.setHasFixedSize(true);
        rvGame.setHasFixedSize(true);

        mList = new ArrayList<>();

        mAdapter = new DiscoverAdapters.AtyAdapter(getContext(), mList);

        rvAty.setAdapter(mAdapter);
        rvGame.setAdapter(mAdapter);

        View emptyView = View.inflate(getContext(), R.layout.layout_error_card, null);
        TextView emptyText = emptyView.findViewById(R.id.error_text);
        emptyText.setText("暂时没有更多活动噢~" + "\n" + "小语正在努力筹划~");
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        emptyView.setLayoutParams(params);
        rvAty.setEmptyView(emptyView);
        rvAty.setPullRefreshEnabled(false);
        rvAty.setLoadingMoreEnabled(false);
        rvAty.setNestedScrollingEnabled(false);

        View mEmptyView = View.inflate(getContext(), R.layout.layout_error_card, null);
        TextView mEmptyText = mEmptyView.findViewById(R.id.error_text);
        mEmptyText.setText("游戏栏目还没运营噢...");
        mEmptyView.setLayoutParams(params);
        rvGame.setEmptyView(mEmptyView);
        rvGame.setPullRefreshEnabled(false);
        rvGame.setLoadingMoreEnabled(false);
        rvGame.setNestedScrollingEnabled(false);
    }

}
