package com.example.carson.yjenglish.tv.view;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.carson.yjenglish.BaseActivity;
import com.example.carson.yjenglish.FullScreenVideo;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.VideoCaptionInfo;
import com.example.carson.yjenglish.VideoCaptionModel;
import com.example.carson.yjenglish.VideoService;
import com.example.carson.yjenglish.adapter.TVListAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.tv.TVInfoContract;
import com.example.carson.yjenglish.tv.TVService;
import com.example.carson.yjenglish.tv.TvInfoTask;
import com.example.carson.yjenglish.tv.model.TVHeader;
import com.example.carson.yjenglish.tv.model.TVInfo;
import com.example.carson.yjenglish.tv.model.TVItem;
import com.example.carson.yjenglish.tv.model.TVMoreInfo;
import com.example.carson.yjenglish.tv.presenter.TVInfoPresenter;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TVFragment extends Fragment implements TVListAdapter.OnItemClickListener,
        TVInfoContract.View, TVListAdapter.OnHeaderClickListener {

    private final String TAG = "TVFragment";

    private static final int REQUEST_TO_DETAIL = 300;
    public static final int RESULT_LIKE_CHANGE = 301;

    private RecyclerView rv;
    private MyVideoView mVideoView;

    private List<TVHeader> mHeaderList;
    private List<TVItem> mItemList;

    private TVListAdapter adapter;

    private boolean isFullClick = false;

    private View lastView;

    private Dialog mDialog;

    private View view;

    private TVInfoContract.Presenter presenter;
    private TVInfoPresenter tvPresenter;

    private List<TVInfo.TvVideo.WordVideo> wordVideos;
    private List<TVInfo.TvVideo.TopVideo> topVideos;

    //根据word_id 读取 播放列表
    private Map<String, List<TVInfo.TvVideo.WordVideo.VideoInfo>> videoList;

    private Map<String, Integer> videoPos = new HashMap<>();

    private List<VideoCaptionModel> mCaptions;

    private int mCurPage = 1;
    private boolean ableToRefresh = true;

    private int clickFeedsPos = 0;

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
        Log.e(TAG, "onCreate");
        mDialog = DialogUtils.getInstance(getContext()).newCommonDialog("加载中",
                R.mipmap.gif_loading_video, true);
        mDialog.setCanceledOnTouchOutside(false);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_tv, container, false);
//        initViews(view);
        executeLoadTask();
        return view;
    }

    private void executeLoadTask() {
        TvInfoTask task = TvInfoTask.getInstance();
        tvPresenter = new TVInfoPresenter(task, this);
        this.setPresenter(tvPresenter);
        presenter.getTvInfo(UserConfig.getToken(getContext()));
    }

    private void initViews(View view) {
        rv = view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new TVListAdapter(getContext(), mItemList, mHeaderList);

        adapter.setItemListener(this);
        adapter.setHeaderlistener(this);

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View lastChildView = recyclerView.getLayoutManager().getChildAt(
                            recyclerView.getLayoutManager().getChildCount() - 1);
                    if (lastChildView != null) {
                        int lastChildBottom = lastChildView.getBottom() + ScreenUtils.dp2px(getContext(), 5);
                        int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();

                        int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

                        if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.
                                getLayoutManager().getItemCount() - 1) {
                            if (ableToRefresh) {
                                Log.e(TAG, "start loading");
                                mCurPage += 1;
                                executeLoadMoreTask();
                            }
                        }
                    }
                }
            }
        });
    }

    private void executeLoadMoreTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(TVService.class).getMoreTvInfo(UserConfig.getToken(getContext()),
                String.valueOf(mCurPage), "4").enqueue(new Callback<TVMoreInfo>() {
            @Override
            public void onResponse(Call<TVMoreInfo> call, Response<TVMoreInfo> response) {
                TVMoreInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    if (info.getData() != null && !info.getData().isEmpty()) {
                        int pos = wordVideos.size();
                        wordVideos.addAll(info.getData());

                        for (int i = pos; i < wordVideos.size(); i++) {
                            videoList.put(wordVideos.get(i).getWord_id(), wordVideos.get(i).getVideo_info());
                            videoPos.put(wordVideos.get(i).getWord_id(), 0);//先全部初始化为0
                        }
                        for (int i = pos; i < wordVideos.size(); i++) {
                            mItemList.add(new TVItem(wordVideos.get(i).getWord_id(),
                                    videoList.get(wordVideos.get(i).getWord_id()).get(0).getVideo(),
                                    wordVideos.get(i).getVideo_info().get(0).getImg(),
                                    wordVideos.get(i).getWord(), wordVideos.get(i).getPhonetic_symbol(),
                                    wordVideos.get(i).getVideo_info().get(0).getComments(),
                                    wordVideos.get(i).getVideo_info().get(0).getFavours(),
                                    wordVideos.get(i).getVideo_info().get(0).getViews(),
                                    videoList.get(wordVideos.get(i).getWord_id()).get(0).getVideo_id(),
                                    false));
                        }
                        adapter.notifyItemRangeChanged(pos, info.getData().size());
                    } else {
                        Toast.makeText(getContext(), "没有更多的咯~", Toast.LENGTH_SHORT).show();
                        ableToRefresh = false;
                    }
                } else {
                    Log.e(TAG, info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<TVMoreInfo> call, Throwable t) {
//                Toast.makeText(getContext(), "连接超时", Toast.LENGTH_SHORT).show();
                DialogUtils utils = DialogUtils.getInstance(getContext());
                Dialog dialog = utils.newTipsDialog("网络加载出现问题了噢...要重试吗", View.TEXT_ALIGNMENT_CENTER);
                dialog.show();

                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.height = ScreenUtils.dp2px(getContext(), 240);
                lp.width = ScreenUtils.dp2px(getContext(), 260);
                lp.y = ScreenUtils.getScreenHeight(getContext()) / 2;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);

                utils.setTipsListener(new DialogUtils.OnTipsListener() {
                    @Override
                    public void onConfirm() {
                        executeLoadMoreTask();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    private void showVideo(final View view, final String word_id, final int position) {
        View v;
        removeVideoView();
        if (mVideoView == null) {
            mVideoView = new MyVideoView(getContext(), true, false, mCaptions);
        }
//        mVideoView.stop();
        mVideoView.release();
        v = view.findViewById(R.id.item_video_play);
        if (v != null) v.setVisibility(View.INVISIBLE);
        v = view.findViewById(R.id.video_bg);
        if (v != null) v.setVisibility(View.INVISIBLE);
        v = view.findViewById(R.id.tv_play_num);
        if (v != null) v.setVisibility(View.INVISIBLE);
        v = view.findViewById(R.id.iv_play_num);
        if (v != null) v.setVisibility(View.INVISIBLE);
        v = view.findViewById(R.id.video);
        if (v != null) {
            v.setVisibility(View.VISIBLE);
            FrameLayout fl = (FrameLayout) v;
            fl.removeAllViews();
            fl.addView(mVideoView, new ViewGroup.LayoutParams(-1, -1));
            String path = videoList.get(word_id).get(videoPos.get(word_id)).getVideo();
            mVideoView.setVideoPath(path);
            mVideoView.start();
        }
        mVideoView.setChangeSourceListener(new MyVideoView.IOnChangeSourceListener() {
            @Override
            public void onFormerClick() {
                if (videoPos.get(word_id) > 0) {
//                    mVideoView.setCaption(mCaptions);
                    executeCaptionTask(view, word_id, videoPos.get(word_id) - 1,
                            videoList.get(word_id).get(videoPos.get(word_id) - 1).getVideo_id());
                    //将pos-1
                    videoPos.put(word_id, videoPos.get(word_id) - 1);

                    TVInfo.TvVideo.WordVideo.VideoInfo info = videoList.get(word_id).get(videoPos.get(word_id));
                    mItemList.get(position).setVideo_id(info.getVideo_id());
                    mItemList.get(position).setCoverImg(info.getImg());
                    mItemList.get(position).setFavour(info.getIs_favour().equals("1"));

                    RecyclerView.ViewHolder holder = rv.findViewHolderForAdapterPosition(position + 1);
                    if (holder != null && holder instanceof TVListAdapter.TVHolder) {
                        final TVListAdapter.TVHolder tvHolder = (TVListAdapter.TVHolder) holder;
                        tvHolder.tvPlayNum.setText(info.getViews());
                        tvHolder.commentNum.setText(info.getComments());
                        tvHolder.likeNum.setText(info.getFavours());
                        tvHolder.ivFavour.setSelected(info.getIs_favour().equals("1"));

                        Glide.with(getContext()).load(info.getImg()).crossFade().listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (tvHolder.videoBg.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                    tvHolder.videoBg.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                return false;
                            }
                        })
                                .thumbnail(0.5f).into(tvHolder.videoBg);
                    }

                    mVideoView.setVideoPath(videoList.get(word_id).get(videoPos.get(word_id)).getVideo());
                    mVideoView.start();
                } else {
                    Toast.makeText(getContext(), "没有上一个视频哦~", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLatterClick() {
                if (videoPos.get(word_id) + 1 < videoList.get(word_id).size()) {
//                    mVideoView.setCaption(mCaptions);
                    executeCaptionTask(view, word_id, videoPos.get(word_id) + 1,
                            videoList.get(word_id).get(videoPos.get(word_id) + 1).getVideo_id());

                    //将pos+1
                    videoPos.put(word_id, videoPos.get(word_id) + 1);

                    TVInfo.TvVideo.WordVideo.VideoInfo info = videoList.get(word_id).get(videoPos.get(word_id));
                    mItemList.get(position).setVideo_id(info.getVideo_id());
                    mItemList.get(position).setCoverImg(info.getImg());
                    mItemList.get(position).setFavour(info.getIs_favour().equals("1"));

                    RecyclerView.ViewHolder holder = rv.findViewHolderForAdapterPosition(position + 1);
                    if (holder != null && holder instanceof TVListAdapter.TVHolder) {
                        final TVListAdapter.TVHolder tvHolder = (TVListAdapter.TVHolder) holder;
                        tvHolder.tvPlayNum.setText(info.getViews());
                        tvHolder.commentNum.setText(info.getComments());
                        tvHolder.likeNum.setText(info.getFavours());
                        tvHolder.ivFavour.setSelected(info.getFavours().equals("1"));

                        Glide.with(getContext()).load(info.getImg()).crossFade().listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (tvHolder.videoBg.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                    tvHolder.videoBg.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                return false;
                            }
                        })
                                .thumbnail(0.5f).into(tvHolder.videoBg);
                    }

                    mVideoView.setVideoPath(videoList.get(word_id).get(videoPos.get(word_id)).getVideo());
                    mVideoView.start();
                } else {
                    Toast.makeText(getContext(), "没有下一个视频噢~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mVideoView.setFullScreenListener(new MyVideoView.IFullScreenListener() {
            @Override
            public void onClickFull(boolean isFull) {
                //if isPlaying 则把progress传过去
                //需要横屏
                isFullClick = true;
                int progress = mVideoView.getPosition();
                Intent toFullScreen = new Intent(getContext(), FullScreenVideo.class);
                toFullScreen.putExtra("progress", progress);
                StringBuilder sb = new StringBuilder();
                StringBuilder stringBuilder = new StringBuilder();
                for (TVInfo.TvVideo.WordVideo.VideoInfo info : videoList.get(word_id)) {
                    sb.append(info.getVideo()).append("；");
                    stringBuilder.append(info.getVideo_id()).append("；");
                }
                String path = sb.toString().substring(0, sb.length() - 1);
                String video_ids = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
                int mCurPos = videoPos.get(word_id);
                toFullScreen.putExtra("path", path);
                toFullScreen.putExtra("video_ids", video_ids);
                toFullScreen.putExtra("current_position", mCurPos);
                toFullScreen.putExtra("has_multi_videos", true);
                toFullScreen.putExtra("caption_type", 1);
                startActivityForResult(toFullScreen, 1);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.anim_top_rotate_get_into,
                            R.anim.anim_top_rotate_sign_out);
                }
            }
        });

        mVideoView.setOnStopListener(new MyVideoView.IOnStopListener() {
            @Override
            public void onVideoStop() {
//                removeVideoView();
//                mVideoView.setVisibility(View.GONE);
//                Log.e(TAG, "onStop");
                if (videoPos.get(word_id) + 1 < videoList.get(word_id).size()) {
//                    mVideoView.setCaption(mCaptions);
                    executeCaptionTask(view, word_id, videoPos.get(word_id) + 1,
                            videoList.get(word_id).get(videoPos.get(word_id) + 1).getVideo_id());
                    videoPos.put(word_id, videoPos.get(word_id) + 1);

                    TVInfo.TvVideo.WordVideo.VideoInfo info = videoList.get(word_id).get(videoPos.get(word_id));
                    mItemList.get(position).setVideo_id(info.getVideo_id());
                    mItemList.get(position).setCoverImg(info.getImg());
                    mItemList.get(position).setFavour(info.getIs_favour().equals("1"));

                    RecyclerView.ViewHolder holder = rv.findViewHolderForAdapterPosition(position + 1);
                    if (holder != null && holder instanceof TVListAdapter.TVHolder) {
                        final TVListAdapter.TVHolder tvHolder = (TVListAdapter.TVHolder) holder;
                        tvHolder.tvPlayNum.setText(info.getViews());
                        tvHolder.commentNum.setText(info.getComments());
                        tvHolder.likeNum.setText(info.getFavours());
                        tvHolder.ivFavour.setSelected(info.getFavours().equals("1"));

                        Glide.with(getContext()).load(info.getImg()).crossFade().listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (tvHolder.videoBg.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                    tvHolder.videoBg.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                return false;
                            }
                        })
                                .thumbnail(0.5f).into(tvHolder.videoBg);
                    }

                    mVideoView.setVideoPath(videoList.get(word_id).get(videoPos.get(word_id)).getVideo());
                    mVideoView.start();
                } else {
                    removeVideoView();
                }
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
            v = lastView.findViewById(R.id.tv_play_num);
            if (v != null) v.setVisibility(View.VISIBLE);
            v = lastView.findViewById(R.id.iv_play_num);
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
                    mVideoView.release();
                }
            } else if (resultCode == FullScreenVideo.RESULT_VIDEO_NOT_FINISH) {
                if (mVideoView != null) {
//                    mVideoView.seekTo(data.getIntExtra("progress", 0));
                    mVideoView.resume();
                }
                isFullClick = false;
            }
        } else if (requestCode == REQUEST_TO_DETAIL && resultCode == RESULT_LIKE_CHANGE) {
            boolean is_favour = data.getBooleanExtra("favour_change", false);
            mItemList.get(clickFeedsPos).setFavour(is_favour);
            RecyclerView.ViewHolder holder = rv.findViewHolderForAdapterPosition(clickFeedsPos + 1);
            if (holder != null && holder instanceof TVListAdapter.TVHolder) {
                TVListAdapter.TVHolder tvHolder = (TVListAdapter.TVHolder) holder;
                tvHolder.ivFavour.setSelected(mItemList.get(clickFeedsPos).isFavour());
                int num = Integer.parseInt(tvHolder.likeNum.getText().toString());
                if (num < 1000) {
                    if (mItemList.get(clickFeedsPos).isFavour()) {
                        if (num + 1 == 1000) {
                            tvHolder.likeNum.setText("1k+");
                        } else {
                            tvHolder.likeNum.setText(String.valueOf(num + 1));
                        }
                    } else {
                        tvHolder.likeNum.setText(String.valueOf(num - 1));
                    }
                } else {
                    if (num > 10000) {
                        tvHolder.likeNum.setText("1w+");
                    } else {
                        String s = String.valueOf(num % 1000) + "k+";
                        tvHolder.likeNum.setText(s);
                    }
                }

            }
        }
    }

    @Override
    public void onVideoClick(View view, String word_id, int position, String video_id) {
//        showVideo(view, word_id, position);
        if (mVideoView != null) {
            showVideo(view, word_id, position);
//            removeVideoView();
//            mVideoView.release();
//            View v;
//            v = view.findViewById(R.id.item_video_play);
//            if (v != null) v.setVisibility(View.INVISIBLE);
//            v = view.findViewById(R.id.video_bg);
//            if (v != null) v.setVisibility(View.INVISIBLE);
//            v = view.findViewById(R.id.tv_play_num);
//            if (v != null) v.setVisibility(View.INVISIBLE);
//            v = view.findViewById(R.id.iv_play_num);
//            if (v != null) v.setVisibility(View.INVISIBLE);
//            v = view.findViewById(R.id.video);
//            if (v.getVisibility() == View.INVISIBLE) {
//                v.setVisibility(View.VISIBLE);
//                FrameLayout fl = (FrameLayout) v;
//                fl.removeAllViews();
//                fl.addView(mVideoView, new ViewGroup.LayoutParams(-1, -1));
//                String path = videoList.get(word_id).get(videoPos.get(word_id)).getVideo();
//                mVideoView.setVideoPath(path);
//                mVideoView.start();
//            }
        }
        executeCaptionTask(view, word_id, position, video_id);

    }

    private void executeCaptionTask(final View view, final String word_id, final int position, String video_id) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(VideoService.class).getVideoInfo(UserConfig.getToken(getContext()),
                video_id).enqueue(new Callback<VideoCaptionInfo>() {
            @Override
            public void onResponse(Call<VideoCaptionInfo> call, Response<VideoCaptionInfo> response) {
                VideoCaptionInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    mCaptions = info.getData();
                    if (mVideoView == null) {
                        showVideo(view, word_id, position);
                    } else {
                        mVideoView.setCaption(mCaptions);
                    }
                } else {
                    Log.e(TAG, info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<VideoCaptionInfo> call, Throwable t) {
                Toast.makeText(getContext(), "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(String video_id, boolean requestComment, int pos, boolean isFavour) {
        if (mVideoView != null) {
            mVideoView.release();
            removeVideoView();
//            Log.e(TAG, "video_stop");
        }
        clickFeedsPos = pos;
        Log.e(TAG, video_id);
        Intent toDetail = new Intent(getContext(), TVItemAty.class);
        toDetail.putExtra("request_comment", requestComment);
        toDetail.putExtra("video_id", video_id);
        toDetail.putExtra("is_favour", isFavour);
        startActivityForResult(toDetail, REQUEST_TO_DETAIL);
        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
        }
    }

    @Override
    public void onFavourClick(String video_id, final TextView tv, final boolean isFavour) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(TVService.class).postFavours(UserConfig.getToken(getContext()),
                video_id).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    if (isFavour) {
                        int num = Integer.parseInt(tv.getText().toString());
                        //取消赞
                        tv.setText(String.valueOf(num - 1));
                    } else {
                        int num = Integer.parseInt(tv.getText().toString());
                        tv.setText(String.valueOf(num + 1));
                    }
                } else {
                    Log.e(TAG, info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Toast.makeText(getContext(), "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
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
            mVideoView.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mVideoView != null) {
            if (isFullClick) {
                mVideoView.pause();
            } else {
                mVideoView.release();
            }
        }
    }

    @Override
    public void setPresenter(TVInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        mDialog.show();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(getContext(), 260);
        lp.height = ScreenUtils.dp2px(getContext(), 240);
        lp.y = ScreenUtils.getScreenHeight(getContext()) / 2 - ScreenUtils.dp2px(getContext(), 260);
        mDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void showError(String msg) {
//        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, msg);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                executeLoadTask();
            }
        }, 5000);
    }

    @Override
    public void onSuccess(TVInfo info) {
        if (info.getStatus().equals("200")) {
            wordVideos = info.getData().getWord_video();
            topVideos = info.getData().getTop_video();

            mHeaderList = new ArrayList<>();
            mItemList = new ArrayList<>();
            for (int i = 0; i < topVideos.size(); i++) {
                mHeaderList.add(new TVHeader(topVideos.get(i).getVideo_id(),
                        topVideos.get(i).getImg(), topVideos.get(i).getWord(),
                        topVideos.get(i).getViews()));
            }
            videoList = new HashMap<>();
            for (int i = 0; i < wordVideos.size(); i++) {
                videoList.put(wordVideos.get(i).getWord_id(), wordVideos.get(i).getVideo_info());
                videoPos.put(wordVideos.get(i).getWord_id(), 0);//先全部初始化为0
            }

            for (int i = 0; i < wordVideos.size(); i++) {
                mItemList.add(new TVItem(wordVideos.get(i).getWord_id(),
                        videoList.get(wordVideos.get(i).getWord_id()).get(0).getVideo(),
                        wordVideos.get(i).getVideo_info().get(0).getImg(),
                        wordVideos.get(i).getWord(), wordVideos.get(i).getPhonetic_symbol(),
                        wordVideos.get(i).getVideo_info().get(0).getComments(),
                        wordVideos.get(i).getVideo_info().get(0).getFavours(),
                        wordVideos.get(i).getVideo_info().get(0).getViews(),
                        videoList.get(wordVideos.get(i).getWord_id()).get(0).getVideo_id(),
                        wordVideos.get(i).getVideo_info().get(0).getIs_favour().equals("1")));
            }
            initViews(view);
        } else {
            Log.e(TAG, info.getMsg());
            if (info.getStatus().equals("400") && info.getMsg().equals("身份认证错误！")) {
                UserConfig.clearUserInfo(getContext());
                SharedPreferences.Editor editor = MyApplication.getContext().
                        getSharedPreferences("word_favours", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                DataSupport.deleteAll(LoadHeader.class);
                BaseActivity.tokenOutOfDate(getActivity());
            }
        }
    }

    @Override
    public void onHeaderClick(String video_id) {
        Intent toItemAty = new Intent(getContext(), TVItemAty.class);
        toItemAty.putExtra("video_id", video_id);
        startActivity(toItemAty);
        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
        }
    }
}
