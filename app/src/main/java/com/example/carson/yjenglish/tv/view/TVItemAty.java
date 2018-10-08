package com.example.carson.yjenglish.tv.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.FullScreenVideo;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.VideoCaptionInfo;
import com.example.carson.yjenglish.VideoCaptionModel;
import com.example.carson.yjenglish.VideoService;
import com.example.carson.yjenglish.adapter.ExpressionAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;
import com.example.carson.yjenglish.home.view.feeds.HomeItemAty;
import com.example.carson.yjenglish.home.viewbinder.feeds.EmptyViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.VideoViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.tv.TVService;
import com.example.carson.yjenglish.tv.model.TVItem;
import com.example.carson.yjenglish.tv.model.TVItemInfo;
import com.example.carson.yjenglish.tv.model.forviewbinder.RecommendList;
import com.example.carson.yjenglish.tv.model.forviewbinder.TVComment;
import com.example.carson.yjenglish.tv.model.forviewbinder.TVRecommendation;
import com.example.carson.yjenglish.tv.viewbinder.RecommendListViewBinder;
import com.example.carson.yjenglish.tv.viewbinder.RecommendViewBinder;
import com.example.carson.yjenglish.tv.viewbinder.TVCommentViewBinder;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TVItemAty extends AppCompatActivity implements VideoViewBinder.OnVideoClickListener,
        RecommendViewBinder.OnRecommendListener, TVCommentViewBinder.OnCommentItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView expressionRv;
    private TextView send;
    private EditText editComment;
    private ImageView expression;

    private RelativeLayout toolbar;
    private TextView back;
    private ImageView favour;
    private ImageView share;

    private MultiTypeAdapter mAdapter;
    private ExpressionAdapter expressionAdapter;
    private Items mItems;

    private boolean requestComment;
    private String video_id;
    private String coverImg;
    private String videoUrl;

    private List<TVItemInfo.TVItemDetail.RecommendVideo> mRecommendations;
    private List<TVRecommendation> tvRecommendations = new ArrayList<>();
    private List<TVComment> mHitComments = new ArrayList<>();
    private List<TVItemInfo.TVItemDetail.HotComment> hotComments;

    private List<VideoCaptionModel> mCaptions;

    private boolean hasComment = false;
    private int mDistance;
    private int maxDistance = 255;

    private InputMethodManager imm;
    private MyVideoView mVideoView;

    private View lastView;

    private boolean isFavour;

    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);
        requestComment = getIntent().getBooleanExtra("request_comment", false);
        video_id = getIntent().getStringExtra("video_id");
        isFavour = getIntent().getBooleanExtra("is_favour", false);
        bindViews();
        executeLoadTask();
//        initRecyclers();
    }

    private void executeLoadTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(TVService.class).getItemInfo(UserConfig.getToken(this), video_id)
                .enqueue(new Callback<TVItemInfo>() {
                    @Override
                    public void onResponse(Call<TVItemInfo> call, Response<TVItemInfo> response) {
                        TVItemInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            mDialog.dismiss();
                            coverImg = info.getData().getTop_video().getImg();
                            videoUrl = info.getData().getTop_video().getVideo();

                            mRecommendations = info.getData().getRecommend_video();

                            hotComments = info.getData().getHot_comment();

                            loadCommentList();

                            initViews();
                        } else {
                            Log.e("TVItemAty", info.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<TVItemInfo> call, Throwable t) {
                        Toast.makeText(TVItemAty.this, "连接超时", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initViews() {
        Log.e("HomeItemAty", "initViews");
        initEditText();
        initRecyclers();
        if (requestComment) {
            editComment.setFocusable(true);
            editComment.setFocusableInTouchMode(true);
            editComment.requestFocus();
            editComment.findFocus();
            imm.showSoftInput(editComment, InputMethodManager.SHOW_FORCED);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            if (!hasComment) {
                recyclerView.smoothScrollToPosition(mItems.size() - 1);
            } else {
                recyclerView.smoothScrollToPosition(4);
            }
        }

    }

    private void initEditText() {
        editComment.addTextChangedListener(new MyTextWatcher());

        editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editComment.setFocusable(true);
                editComment.setFocusableInTouchMode(true);
                editComment.requestFocus();
                editComment.findFocus();
                if (!hasComment) {
                    recyclerView.smoothScrollToPosition(mItems.size() - 1);
                } else {
                    recyclerView.smoothScrollToPosition(3);
                }
                imm.showSoftInput(editComment, InputMethodManager.SHOW_FORCED);
            }
        });

        expression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
                }
                expressionRv.setVisibility(View.VISIBLE);
                expressionRv.setLayoutManager(new GridLayoutManager(TVItemAty.this, 5));
                expressionAdapter = new ExpressionAdapter(TVItemAty.this);
                expressionRv.setAdapter(expressionAdapter);
            }
        });
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        expressionRv = findViewById(R.id.expression_recycler);
        send = findViewById(R.id.send);
        editComment = findViewById(R.id.edit_comment);
        expression = findViewById(R.id.expression);

        back = findViewById(R.id.back);
        favour = findViewById(R.id.iv_favour);
        share = findViewById(R.id.share);
        toolbar = findViewById(R.id.toolbar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        favour.setSelected(isFavour);
        favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFavourTask();
            }
        });

        mDialog = DialogUtils.getInstance(this).newCommonDialog("加载中",
                R.mipmap.gif_loading_video, true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 240);
        mDialog.getWindow().setAttributes(lp);
    }

    private void executeFavourTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(TVService.class).postFavours(UserConfig.getToken(this),
                video_id).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    isFavour = !isFavour;
                    favour.setSelected(isFavour);
                    Intent backIntent = new Intent();
                    backIntent.putExtra("favour_change", isFavour);
                    setResult(TVFragment.RESULT_LIKE_CHANGE, backIntent);
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Toast.makeText(TVItemAty.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclers() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mDistance += dy;
                float percent = mDistance * 0.5f / maxDistance;
                int alpha = (int) (percent * 255);
                setToolbarAlpha(alpha);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);//滑动时隐藏输入法
                }
                editComment.setFocusable(false);
                expressionRv.setVisibility(View.GONE);
                editComment.setHint("发表评论");
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mVideoView != null && mVideoView.isPlaying()) {
                    toolbar.setVisibility(View.VISIBLE);
                } else {
                    ObjectAnimator animator;
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        toolbar.setVisibility(View.VISIBLE);
                        animator = ObjectAnimator.ofFloat(toolbar, "translationY", -toolbar.getHeight(),
                                0);
                        animator.setDuration(300).start();
                    } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        animator = ObjectAnimator.ofFloat(toolbar, "translationY", 0,
                                -toolbar.getHeight());
                        animator.setDuration(300).start();
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                toolbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            }
        });

        mAdapter.register(Video.class, new VideoViewBinder(this));
        mAdapter.register(EmptyValue.class).to(new FieldTitleViewBinder(), new EmptyViewBinder())
                .withClassLinker(new ClassLinker<EmptyValue>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<EmptyValue, ?>> index(@NonNull EmptyValue emptyValue) {
                        if (emptyValue.getDrawableRes() != 0) {
                            return EmptyViewBinder.class;
                        }
                        return FieldTitleViewBinder.class;
                    }
                });
        mAdapter.register(RecommendList.class, new RecommendListViewBinder(this));
        mAdapter.register(TVComment.class, new TVCommentViewBinder(this));

        mItems.add(new Video(video_id, coverImg, videoUrl));
        mItems.add(new EmptyValue("推荐语境"));
        for (int i = 0; i < mRecommendations.size(); i++) {
            tvRecommendations.add(new TVRecommendation(mRecommendations.get(i).getWord(),
                    mRecommendations.get(i).getViews(), mRecommendations.get(i).getVideo_id(),
                    mRecommendations.get(i).getImg()));
        }
        mItems.add(new RecommendList(tvRecommendations));
        if (hasComment) {
            initComment(false);
        } else {
            mItems.add(new EmptyValue("还没有评论，快来抢沙发！", R.mipmap.bg_plan_box));
        }

        mItems.add(new EmptyValue(""));
        mItems.add(new EmptyValue(""));

        mAdapter.notifyDataSetChanged();

    }

    private void setToolbarAlpha(int alpha) {
        if (alpha >= 255) {
            alpha = 255;
        }
        toolbar.getBackground().setAlpha(alpha);
    }

    private void initComment(boolean isRefresh) {
        if (isRefresh) {
            mItems.removeAll(mHitComments);
            int length = mAdapter.getItemCount();
            for (int i = length - 1; mAdapter.getItemCount() - 3 > 0; i-- ) {
                mItems.remove(i);
            }
            firstTimeInitComment();
            mItems.add(new EmptyValue(""));
            mItems.add(new EmptyValue(""));
            mAdapter.notifyItemRangeChanged(3, mItems.size() - 3);
        } else {
            firstTimeInitComment();
        }
    }

    private void firstTimeInitComment() {
        if (hotComments.size() != 0) {
            mItems.add(new EmptyValue("热门评论"));
            mItems.addAll(mHitComments);
        }
    }

    private void doSendWork() {
        if (!hasComment) {
            //将原本没有评论的emptyView去除
            mItems.remove(3);
            hasComment = true;
        }
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(TVService.class).sendComment(UserConfig.getToken(this),
                video_id, editComment.getText().toString()).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                if (response.body().getStatus().equals("200")) {
                    refreshCommentList();
                } else {
                    Log.e("TVItemAty", response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Toast.makeText(TVItemAty.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
        }
        editComment.setFocusable(false);
        expressionRv.setVisibility(View.GONE);
    }

    private void refreshCommentList() {
        NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST)
                .create(TVService.class).getItemInfo(UserConfig.getToken(this), video_id)
                .enqueue(new Callback<TVItemInfo>() {
                    @Override
                    public void onResponse(Call<TVItemInfo> call, Response<TVItemInfo> response) {
                        TVItemInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            hotComments.clear();
                            hotComments.addAll(info.getData().getHot_comment());

                            editComment.setText("");
                            initComment(true);

                            loadCommentList();
                        } else {
                            Log.e("TVItemAty", info.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<TVItemInfo> call, Throwable t) {
                        Toast.makeText(TVItemAty.this, "连接超时", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCommentList() {
        mHitComments.clear();
        for (int i = 0; i < hotComments.size(); i++) {
            mHitComments.add(new TVComment(hotComments.get(i).getSet_time(),
                    hotComments.get(i).getIs_like(), hotComments.get(i).getComment(),
                    "200", hotComments.get(i).getId(), hotComments.get(i).getUser_id(),
                    hotComments.get(i).getPortrait(), hotComments.get(i).getUsername()));
        }
    }

    private void showVideo(View view, final String path) {
        View v;
        removeVideoView();
        if (mVideoView == null) {
            mVideoView = new MyVideoView(this, false, false, mCaptions);
        }
        mVideoView.release();
        v = view.findViewById(R.id.item_video_play);
        if (v != null) v.setVisibility(View.INVISIBLE);
        v = view.findViewById(R.id.img);
        if (v != null) v.setVisibility(View.INVISIBLE);
        v = view.findViewById(R.id.video);
        if (v != null) {
            v.setVisibility(View.VISIBLE);
            FrameLayout fl = (FrameLayout) v;
            fl.removeAllViews();
            fl.addView(mVideoView, new ViewGroup.LayoutParams(-1, -1));
//            mVideoView.setCaption(mCaptions);
            mVideoView.setVideoPath(path);
            mVideoView.start();
        }
        mVideoView.setFullScreenListener(new MyVideoView.IFullScreenListener() {
            @Override
            public void onClickFull(boolean isFull) {
                //if isPlaying 则把progress传过去
                //需要横屏
                int progress = mVideoView.getPosition();
                Intent toFullScreen = new Intent(TVItemAty.this, FullScreenVideo.class);
                toFullScreen.putExtra("video_ids", video_id);
                toFullScreen.putExtra("caption_type", 1);
                toFullScreen.putExtra("progress", progress);
                toFullScreen.putExtra("path", path);
                startActivityForResult(toFullScreen, 1);
                overridePendingTransition(R.anim.anim_top_rotate_get_into, R.anim.anim_top_rotate_sign_out);
            }
        });

        mVideoView.setOnStopListener(new MyVideoView.IOnStopListener() {
            @Override
            public void onVideoStop() {
                removeVideoView();
            }
        });

        lastView = view;
    }

    private void removeVideoView() {
        View v;
        if (lastView != null) {
            v = lastView.findViewById(R.id.item_video_play);
            if (v != null) v.setVisibility(View.VISIBLE);
            v = lastView.findViewById(R.id.img);
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
    public void onVideoClick(View view, String url) {
        if (mVideoView != null) {
            showVideo(view, url);
        } else {
            executeCaptionTask(view, url);
        }
    }

    private void executeCaptionTask(final View view, final String url) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(VideoService.class).getVideoInfo(UserConfig.getToken(this),
                video_id).enqueue(new Callback<VideoCaptionInfo>() {
            @Override
            public void onResponse(Call<VideoCaptionInfo> call, Response<VideoCaptionInfo> response) {
                VideoCaptionInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    mCaptions = info.getData();
                    showVideo(view, url);
                } else {
                    Log.e("TVItemAty", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<VideoCaptionInfo> call, Throwable t) {
                Toast.makeText(TVItemAty.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mVideoView != null) {
            mVideoView.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.release();
            mVideoView = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == FullScreenVideo.RESULT_VIDEO_COMPLETE) {
                if (mVideoView != null) {
                    mVideoView.stop();
                }
            } else if (resultCode == FullScreenVideo.RESULT_VIDEO_NOT_FINISH) {
                if (mVideoView != null) {
                    mVideoView.seekTo(data.getIntExtra("progress", 0));
                    mVideoView.resume();
                }
            }
        }
    }

    /**
     * 推荐视频的点击事件
     * @param video_id 视频id
     */
    @Override
    public void onRecommendClick(String video_id) {
        Intent toTVItemAty = new Intent(this, TVItemAty.class);
        toTVItemAty.putExtra("video_id", video_id);
        startActivity(toTVItemAty);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    /**
     * 评论的点赞响应
     * @param id 评论的id
     */
    @Override
    public void onLikeClick(String id) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
//        retrofit.create(TVService.class).postFavours(UserConfig.getToken(this), id)
    }

    /**
     * 评论的提示框显示 显示删除还是举报
     * @param id 评论的id
     */
    @Override
    public void onMoreClick(String id) {

    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable)) {
                send.setEnabled(true);
                ViewGroup.LayoutParams lp = editComment.getLayoutParams();
                lp.width = ScreenUtils.dp2px(TVItemAty.this, 290);
                editComment.setLayoutParams(lp);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doSendWork();
                    }
                });
            } else {
                send.setEnabled(false);
                ViewGroup.LayoutParams lp = editComment.getLayoutParams();
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                editComment.setLayoutParams(lp);
            }
        }

    }

}
