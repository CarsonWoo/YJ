package com.example.carson.yjenglish.tv.view;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.carson.yjenglish.FullScreenVideo;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.TVListAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.tv.model.TVHeader;
import com.example.carson.yjenglish.tv.model.TVItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TVFragment extends Fragment implements TVListAdapter.OnItemClickListener {

    private final String TAG = "TVFragment";

    private RecyclerView rv;
    private MyVideoView mVideoView;

    private List<TVHeader> mHeaderList;
    private List<TVItem> mItemList;

    private TVListAdapter adapter;

    private boolean isFullClick = false;

    private View lastView;

    public TVFragment() {
        // Required empty public constructor
    }

    public static TVFragment newInstance() {
        TVFragment fragment = new TVFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rv = view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mHeaderList = new ArrayList<>();
        mItemList = new ArrayList<>();

        TVHeader header = new TVHeader();
        header.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg");
        header.setPlayNum("1.3W");
        header.setWord("travel");
        mHeaderList.add(header);
        TVHeader header1 = new TVHeader();
        header1.setWord("color");
        header1.setPlayNum("999+");
        header1.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg");
        mHeaderList.add(header1);

        TVItem tvItem = new TVItem();
        tvItem.setWord("travel");
        tvItem.setSoundMark("[ˈtrævəl]");
        tvItem.setCommentNum("321");
        tvItem.setPlayNum("1.3w");
        tvItem.setLikeNum("999+");
        tvItem.setVideoUrl("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4");
        for (int i = 0; i < 4; i ++) {
            mItemList.add(tvItem);
        }

        adapter = new TVListAdapter(getContext());
        adapter.setmHeaderList(mHeaderList);
        adapter.setmItemList(mItemList);
        adapter.setItemListener(this);

        rv.setAdapter(adapter);

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
        v = view.findViewById(R.id.video_bg);
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
            v = lastView.findViewById(R.id.video_bg);
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
        }
    }

    @Override
    public void onVideoClick(View view, String path) {
        showVideo(view, path);
    }

    @Override
    public void onItemClick(View view) {

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
        if (mVideoView != null) {
            if (isFullClick) {
                mVideoView.pause();
            } else {
                mVideoView.stop();
            }
        }
    }
}
