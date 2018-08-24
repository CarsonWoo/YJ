package com.example.carson.yjenglish.music.view;

import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.MusicListAdapters;
import com.example.carson.yjenglish.music.model.MusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMusicSelectListener} interface
 * to handle interaction events.
 */
public class LikeTab extends Fragment {

    private TextView count;
    private TextView downLoadAll;
    private PullToRefreshRecyclerView recyclerView;

    private OnMusicSelectListener mListener;

    private MusicListAdapters.LikeAdapter mAdapter;

    private List<MusicBean> mList;

    public LikeTab() {
        // Required empty public constructor
    }

    public static LikeTab newInstance() {
        LikeTab fragment = new LikeTab();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.layout_music_fragment, container, false);
        initViews(mView);
        return mView;
    }

    private void initViews(View view) {
        count = view.findViewById(R.id.music_count);
        downLoadAll = view.findViewById(R.id.download_all_music);
        recyclerView = view.findViewById(R.id.recycler_view);

        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MusicBean musicBean = new MusicBean();
            musicBean.setMusicUrl("");
            musicBean.setWord("i" + i);
            mList.add(musicBean);
        }
        mAdapter = new MusicListAdapters.LikeAdapter(getContext(), mList, mListener);

        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setLoadMoreComplete();
                    }
                }, 1000);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMusicSelectListener) {
            mListener = (OnMusicSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMusicSelectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMusicSelectListener {
        void onMusicSelect(String path);
    }
}
