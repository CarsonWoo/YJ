package com.example.carson.yjenglish.home.view;


import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.HomeListAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.home.model.HomeItem;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.home.model.PicHeader;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeListAdapter.OnVideoListener {

    private final String TAG = "HomeFragment";

    private RecyclerView recyclerView;
    private FrameLayout mFullScreenFl;
    private MyVideoView mVideoView;

    private int mHeaderStyle;

    private View lastView;
    private int videoPos = -1;

    private RecyclerView.LayoutManager mLayoutManager;
    private HomeListAdapter mAdapter;

    private OnHomeInteractListener mListener;
    private PicHeader mPicData;
    private LoadHeader mLoadData;
    private List<HomeItem> mListData;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        //如果有数据需要传输 则通过参数 设置bundle
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //作UI请求
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        initEvents();
        return view;
    }

    private void initEvents() {

    }

    private void showVideo(View view, final String path) {
        View v;
        removeVideoView();
        if (mVideoView == null) {
            mVideoView = new MyVideoView(getContext());
        }
        mVideoView.stop();
        v = view.findViewById(R.id.item_video_play);
        if (v != null) v.setVisibility(View.INVISIBLE);
        v = view.findViewById(R.id.video);
        if (v != null) {
            v.setVisibility(View.VISIBLE);
            FrameLayout fl = (FrameLayout) v;
            fl.removeAllViews();
            fl.addView(mVideoView, new ViewGroup.LayoutParams(-1, -1));
            mVideoView.setVideoPath(path);
            mVideoView.start();
        }
        mVideoView.setFullScreenListener(new MyVideoView.IFullScreenListener() {
            @Override
            public void onClickFull(boolean isFull) {
                //TODO 跳转到新的Activity播放
                //if isPlaying 则把progress传过去
            }
        });

        mVideoView.setOnStopListener(new MyVideoView.IOnStopListener() {
            @Override
            public void onVideoStop() {
                removeVideoView();
//                mVideoView.setVisibility(View.GONE);
                Log.e(TAG, "onStop");
            }
        });

        lastView = view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstance");
    }

    private void removeVideoView() {
        View v;
        if (lastView != null) {
            v = lastView.findViewById(R.id.item_video_play);
            if (v != null) v.setVisibility(View.VISIBLE);
            v = lastView.findViewById(R.id.video);
            if (v != null) {
                FrameLayout fl = (FrameLayout) v;
                fl.removeAllViews();
                fl.setVisibility(View.GONE);
            }
        }
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_container);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        mFullScreenFl = view.findViewById(R.id.fl_full_screen);

        mListData = new ArrayList<>();
        mPicData = new PicHeader();
        mPicData.setImgUrl("http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg");
        mPicData.setNumber("45,899");

        for (int i = 0; i < 10; i++) {
            HomeItem data = new HomeItem();
            data.setCommentNum(i * 10);
            data.setLikeNum(i * 20);
            data.setUsername("Carson");
            if (i == 0 || i == 1) {
                data.setVideoUrl("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4");
            } else {
                data.setImgUrl("http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg");
            }
            mListData.add(data);
        }
//        prepareVideo();

        mHeaderStyle = 3;
        if (mHeaderStyle == 2) {
            mAdapter = new HomeListAdapter(getContext(), mListData, mListener, 2, this);
            initAdapterListener(2);
            mAdapter.setPicItem(mPicData);
        } else {
            mAdapter = new HomeListAdapter(getContext(), mListData, mListener, 3, this);
            LitePal.getDatabase();
            //以下这些都要放在联网后再设置
            mLoadData = DataSupport.find(LoadHeader.class, 1);//id从1开始记录
            if (mLoadData == null) {
                Log.e(TAG, "new Instance");
                mLoadData = new LoadHeader();
                mLoadData.setCountDown(30);
                mLoadData.setInsistCount(5);
                mLoadData.setTargetCount(200);
                mLoadData.setTodayFinish(false);
                mLoadData.setProgress(10);
                mLoadData.setWordsCount(2000);
                mLoadData.save();
            }
            Log.e(TAG, mLoadData.isSaved() + "");
            mAdapter.setLoadItem(mLoadData);
            initAdapterListener(3);
        }

        recyclerView.setAdapter(mAdapter);


    }

    private void initAdapterListener(int style) {
        if (style == 2) {
            mAdapter.setStartListener(new HomeListAdapter.OnStartListener() {
                @Override
                public void onStartClick(View view) {
                    Log.i(TAG, "Pic Start Click");
                }
            });
        } else {
            mAdapter.setStartListener(new HomeListAdapter.OnStartListener() {
                @Override
                public void onStartClick(View view) {
                    Log.i(TAG, "Load Start Click");
                }
            });
            mAdapter.setLoadListener(new HomeListAdapter.OnLoadHeaderListener() {
                @Override
                public void onSignClick(View view) {
                    Log.i(TAG, "sign click");
                }

                @Override
                public void onMoreClick(View view) {
                    Log.i(TAG, "more click");
                }
            });
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mVideoView != null) {
                mVideoView.pause();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mVideoView != null) {
            mVideoView.stop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mVideoView != null) {
            mVideoView.stop();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeInteractListener) {
            mListener = (OnHomeInteractListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeInteractListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onVideoClick(View view, String url) {
//        prepareVideo(view, url);
        showVideo(view, url);
    }

    public interface OnHomeInteractListener {
        void onMusicPressed(View view);
        void onItemClick(ArrayList item);
        void onProgressClick(View view);
    }

}
