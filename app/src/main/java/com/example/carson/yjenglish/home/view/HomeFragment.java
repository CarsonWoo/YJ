package com.example.carson.yjenglish.home.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.carson.yjenglish.FullScreenVideo;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.HomeListAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.home.model.HomeItem;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.home.model.PicHeader;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeListAdapter.OnVideoListener {

    private final String TAG = "HomeFragment";

    public static final int REQUEST_WORD_CODE = 101;
    public static final int REQUEST_SIGN_CODE = 102;
    public static final int REQUEST_MORE_CODE = 103;
    public static final int RESULT_WORD_OK = 201;
    public static final int RESULT_SIGN_OK = 202;

    private RecyclerView recyclerView;
    private MyVideoView mVideoView;

    private int mHeaderStyle;

    private View lastView;
    private int videoPos = -1;

    private RecyclerView.LayoutManager mLayoutManager;
    private HomeListAdapter mAdapter;

    private OnHomeInteractListener mListener;
    private OnFullScreenListener fullScreenListener;
    private PicHeader mPicData;
    private LoadHeader mLoadData;
    private List<HomeItem> mListData;

    private boolean isFullClick = false;

    private String mCurDate;

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
        mCurDate = UserConfig.getLastDate(getContext());
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
        v = view.findViewById(R.id.img_content);
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
                //if isPlaying 则把progress传过去
                //需要横屏
                isFullClick = true;
                int progress = mVideoView.getPosition();
                Intent toFullScreen = new Intent(getContext(), FullScreenVideo.class);
                toFullScreen.putExtra("progress", progress);
                toFullScreen.putExtra("path", path);
                startActivityForResult(toFullScreen, 1);
                getActivity().overridePendingTransition(R.anim.anim_top_rotate_get_into, R.anim.anim_top_rotate_sign_out);
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


    private void removeVideoView() {
        View v;
        if (lastView != null) {
            v = lastView.findViewById(R.id.item_video_play);
            if (v != null) v.setVisibility(View.VISIBLE);
            v = lastView.findViewById(R.id.img_content);
            if (v != null) v.setVisibility(View.VISIBLE);
            v = lastView.findViewById(R.id.video);
            if (v != null) {
                FrameLayout fl = (FrameLayout) v;
                fl.removeAllViews();
                fl.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstance");
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_container);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

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
                data.setImgUrl("http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg");
                data.setPortraitUrl("http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg");
            } else {
                data.setImgUrl("http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg");
                data.setPortraitUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg");
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
            Date mDate = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
            String dateStr = df.format(mDate);
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
                mLoadData.setSignClick(false);
                mLoadData.save();
            }
            Log.e(TAG, "mCurDate = " + mCurDate);
            Log.e(TAG, "dateStr = " + dateStr);
            if (!dateStr.equals(mCurDate)) {
                mLoadData.setSignClick(false);
                mLoadData.setTodayFinish(false);
                mLoadData.save();
            }
//            Log.e(TAG, mLoadData.isSaved() + "");
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
                    Intent toWord = new Intent(getContext(), WordActivity.class);
                    toWord.putExtra("type", REQUEST_WORD_CODE);
                    startActivityForResult(toWord, REQUEST_WORD_CODE);
                    if (getActivity() != null) {
                        getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    }
                }
            });
            mAdapter.setLoadListener(new HomeListAdapter.OnLoadHeaderListener() {
                @Override
                public void onSignClick(View view) {
                    Intent toSignIn = new Intent(getContext(), SignInAty.class);
                    startActivityForResult(toSignIn, REQUEST_SIGN_CODE);
                    startActivity(new Intent(getContext(), SignAty.class));
                    if (getActivity() != null) {
                        getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    }
                }

                @Override
                public void onMoreClick(View view) {
                    Intent toWord = new Intent(getContext(), WordActivity.class);
                    toWord.putExtra("type", REQUEST_MORE_CODE);
                    startActivityForResult(toWord, REQUEST_MORE_CODE);
                    if (getActivity() != null) {
                        getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == FullScreenVideo.RESULT_VIDEO_COMPLETE) {
                isFullClick = false;
                if (mVideoView != null) {
                    mVideoView.stop();
                }
            } else if (resultCode == FullScreenVideo.RESULT_VIDEO_NOT_FINISH) {
                if (mVideoView != null) {
                    mVideoView.seekTo(data.getIntExtra("progress", 0));
                    mVideoView.start();
                }
                isFullClick = false;
            }
        } else if (requestCode == REQUEST_WORD_CODE && resultCode == RESULT_WORD_OK) {
            mLoadData.setTodayFinish(true);
            mLoadData.save();
            mAdapter.notifyItemChanged(0);
        } else if (requestCode == REQUEST_SIGN_CODE && resultCode == RESULT_SIGN_OK) {
//            mLoadData.setSignClick(true);
//            mLoadData.save();
//            mAdapter.notifyItemChanged(0);
        } else if (requestCode == REQUEST_MORE_CODE && resultCode == getActivity().RESULT_OK) {
            Log.e(TAG, "progress finish");
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
        Log.e(TAG, "isFullClick = " + isFullClick);
        Log.e(TAG, "onStop");
        if (mVideoView != null) {
            if (isFullClick) {
                mVideoView.pause();
            } else {
                mVideoView.stop();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "isFullClick = " + isFullClick);
        if (mVideoView != null) {
            if (isFullClick) {
                mVideoView.pause();
            } else {
                mVideoView.stop();
            }
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
        void onItemClick(ArrayList item, boolean requestComment);
        void onProgressClick(View view);
    }

    public interface OnFullScreenListener {
        void onFullScreen(String path, int progress);
    }

}
