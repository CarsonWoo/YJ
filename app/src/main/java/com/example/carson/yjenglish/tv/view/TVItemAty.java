package com.example.carson.yjenglish.tv.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Handler;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.FullScreenVideo;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.VideoCaptionInfo;
import com.example.carson.yjenglish.VideoCaptionModel;
import com.example.carson.yjenglish.VideoService;
import com.example.carson.yjenglish.adapter.ExpressionAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.discover.view.DiscoverFragment;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;
import com.example.carson.yjenglish.home.view.feeds.HomeItemAty;
import com.example.carson.yjenglish.home.viewbinder.feeds.EmptyViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.VideoViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.tv.TVService;
import com.example.carson.yjenglish.tv.model.LoadCommentInfo;
import com.example.carson.yjenglish.tv.model.TVItem;
import com.example.carson.yjenglish.tv.model.TVItemInfo;
import com.example.carson.yjenglish.tv.model.forviewbinder.RecommendList;
import com.example.carson.yjenglish.tv.model.forviewbinder.TVComment;
import com.example.carson.yjenglish.tv.model.forviewbinder.TVRecommendation;
import com.example.carson.yjenglish.tv.viewbinder.LoadMoreViewBinder;
import com.example.carson.yjenglish.tv.viewbinder.RecommendListViewBinder;
import com.example.carson.yjenglish.tv.viewbinder.RecommendViewBinder;
import com.example.carson.yjenglish.tv.viewbinder.TVCommentViewBinder;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
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

/**
 * 适配没做好
 * 点击评论了之后响应很慢
 * 且显示不出
 */

public class TVItemAty extends AppCompatActivity implements VideoViewBinder.OnVideoClickListener,
        RecommendViewBinder.OnRecommendListener, TVCommentViewBinder.OnCommentItemClickListener, LoadMoreViewBinder.OnLoadListener {

    private enum DOWN_LOAD_TYPE {
        SHARE_QQ, SHARE_WECHAT, SHARE_QZONE, SHARE_TIMELINE
    }

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
    private List<TVComment> mLatestComments = new ArrayList<>();
    private List<TVItemInfo.TVItemDetail.HotComment> newComments;

    private List<VideoCaptionModel> mCaptions;

    private boolean hasComment = false;
    private int mDistance;
    private int maxDistance = 255;

    private InputMethodManager imm;
    private MyVideoView mVideoView;

    private View lastView;

    private boolean isFavour;

    private Dialog mDialog;

    private Dialog commentDialog;

    private int mPage = 1;

    private boolean isLoadMoreEnable = true;

    private IWXAPI mWxApi;
    private Tencent mTencent;
    private ShareListener shareListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        getWindow().setStatusBarColor(Color.BLACK);
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_tv_detail);
        requestComment = getIntent().getBooleanExtra("request_comment", false);
        video_id = getIntent().getStringExtra("video_id");

        isFavour = getIntent().getBooleanExtra("is_favour", false);

        mTencent = Tencent.createInstance(UserConfig.QQ_APP_ID, MyApplication.getContext());
        mWxApi = MyApplication.mWXApi;
        shareListener = new ShareListener();

        commentDialog = DialogUtils.getInstance(this).newCommonDialog("正在上传评论中,请稍候...",
                R.mipmap.gif_loading_video, true);
        commentDialog.setCanceledOnTouchOutside(false);

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

                            isFavour = info.getData().getIs_favour().equals("1");

                            favour.setSelected(isFavour);

                            mRecommendations = info.getData().getRecommend_video();

                            hotComments = info.getData().getHot_comment();

                            newComments = info.getData().getNew_comment();

                            if (hotComments.size() != 0 || newComments.size() != 0) {
                                hasComment = true;
                            }

                            loadCommentList();

                            initViews();
                        } else {
                            Log.e("TVItemAty", info.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<TVItemInfo> call, Throwable t) {
                        Toast.makeText(TVItemAty.this, "连接超时 正在重试", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                executeLoadTask();
                            }
                        }, 5000);
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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareWindow();
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

    private void showShareWindow() {
        View windowView = LayoutInflater.from(this).inflate(R.layout.layout_share_popup_window,
                null, false);
        final PopupWindow window = new PopupWindow(windowView, ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.dp2px(this, 110));
        window.setAnimationStyle(R.style.popup_window_photo_utils_animation);
        TextView cancel = windowView.findViewById(R.id.cancel_share);
        LinearLayout wechatShare = windowView.findViewById(R.id.share_wechat);
        LinearLayout timelineShare = windowView.findViewById(R.id.share_timeline);
        LinearLayout qqShare = windowView.findViewById(R.id.share_qq);
        LinearLayout qzoneShare = windowView.findViewById(R.id.share_qzone);

        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.showAsDropDown(getWindow().getDecorView(), 0, -window.getHeight(), Gravity.BOTTOM);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window.isShowing()) {
                    window.dismiss();
                }
            }
        });

        qqShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFileEnable(DOWN_LOAD_TYPE.SHARE_QQ);
                window.dismiss();
            }
        });

        qzoneShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFileEnable(DOWN_LOAD_TYPE.SHARE_QZONE);
                window.dismiss();
            }
        });

        wechatShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFileEnable(DOWN_LOAD_TYPE.SHARE_WECHAT);
                window.dismiss();
            }
        });

        timelineShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFileEnable(DOWN_LOAD_TYPE.SHARE_TIMELINE);
                window.dismiss();
            }
        });
    }

    private void checkFileEnable(DOWN_LOAD_TYPE type) {
        if (type == DOWN_LOAD_TYPE.SHARE_QQ || type == DOWN_LOAD_TYPE.SHARE_QZONE) {
            if (!mTencent.isQQInstalled(MyApplication.getContext())) {
                Toast.makeText(MyApplication.getContext(), "请先安装QQ客户端", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (!mWxApi.isWXAppInstalled()) {
                Toast.makeText(MyApplication.getContext(), "请先安装微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        executeShareTask(type);

    }

    private void executeShareTask(DOWN_LOAD_TYPE type) {
        switch (type) {
            case SHARE_QQ:
                executeShare2QQ();
                break;
            case SHARE_QZONE:
                executeShare2QZone();
                break;
            case SHARE_WECHAT:
                executeShare2Wechat();
                break;
            case SHARE_TIMELINE:
                executeShare2Timeline();
                break;
            default:
                break;
        }
    }

    private void executeShare2Timeline() {
        WXVideoObject videoObject = new WXVideoObject();

        videoObject.videoUrl = videoUrl;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = videoObject;
        msg.title = "背呗视频";
        msg.description = "边学单词边看剧，看看歪果仁是怎么用词的~";

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                android.R.drawable.dialog_frame);
        msg.thumbData = getWXThumb(bitmap);
        SendMessageToWX.Req  req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mWxApi.sendReq(req);
    }

    private byte[] getWXThumb(Bitmap thumbBitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int quality = 70;
        thumbBitmap.compress(Bitmap.CompressFormat.JPEG, quality, output);
        byte[] result = output.toByteArray();
        return result;
    }

    private void executeShare2Wechat() {
        WXVideoObject videoObject = new WXVideoObject();

        videoObject.videoUrl = videoUrl;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = videoObject;
        msg.title = "背呗视频";
        msg.description = "边学单词边看剧，看看歪果仁是怎么用词的~";

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                android.R.drawable.dialog_frame);
        msg.thumbData = getWXThumb(bitmap);
        SendMessageToWX.Req  req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mWxApi.sendReq(req);
    }

    private void executeShare2QZone() {
        Toast.makeText(getApplicationContext(), "QQ空间不支持分享视频噢~", Toast.LENGTH_SHORT).show();
    }

    private void executeShare2QQ() {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "背呗视频");
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, videoUrl);
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "边学单词边看剧，看看歪果仁是怎么用词的~");
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, coverImg);
            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "背呗背单词");
            bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");

            mTencent.shareToQQ(this, bundle, shareListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        mAdapter.register(Text.class, new LoadMoreViewBinder(this));

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

        if (mLatestComments.size() >= 15) {
            mItems.add(new Text("加载更多评论", false));
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
            mItems.removeAll(newComments);
            int length = mAdapter.getItemCount();
            for (int i = length - 1; mAdapter.getItemCount() - 3 > 0; i-- ) {
                mItems.remove(i);
            }
            firstTimeInitComment();
            if (mLatestComments.size() >= 15) {
                mItems.add(new Text("加载更多评论", false));
            }
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
        if (newComments.size() != 0) {
            mItems.add(new EmptyValue("最新评论"));
            mItems.addAll(mLatestComments);
        }
    }

    private void doSendWork() {
        if (!hasComment) {
            //将原本没有评论的emptyView去除
            mItems.remove(3);
            hasComment = true;
        }
        commentDialog.show();
        WindowManager.LayoutParams lp = commentDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 240);
        lp.gravity = Gravity.CENTER;
        commentDialog.getWindow().setAttributes(lp);
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
                            newComments.clear();
                            newComments.addAll(info.getData().getNew_comment());

                            loadCommentList();

                            commentDialog.dismiss();
                            editComment.setText("");
                            initComment(true);

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
                    "0", hotComments.get(i).getId(), hotComments.get(i).getUser_id(),
                    hotComments.get(i).getPortrait(), hotComments.get(i).getUsername()));
        }
        mLatestComments.clear();
        for (int i = 0; i < newComments.size(); i++) {
            mLatestComments.add(new TVComment(newComments.get(i).getSet_time(),
                    newComments.get(i).getIs_like(), newComments.get(i).getComment(),
                    "0", newComments.get(i).getId(), newComments.get(i).getUser_id(),
                    newComments.get(i).getPortrait(), newComments.get(i).getUsername()));
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
    public void onLikeClick(String id, final TextView textView, final String is_like) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(TVService.class).postCommentFavours(UserConfig.getToken(this), id)
                .enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
//                            Log.e("TVItemAty", "点赞成功");
                            boolean isLike = is_like.equals("1");
                            int num;
                            if (textView.getText().toString().equals("") || textView.getText().toString().isEmpty()) {
                                num = 0;
                            } else {
                                if (textView.getText().toString().endsWith("+")) {
                                    num = 10000;
                                } else {
                                    num = Integer.parseInt(textView.getText().toString());
                                }
                            }
                            if (num < 1000) {
                                if (isLike) {
                                    if (num + 1 == 1000) {
                                        textView.setText("1k+");
                                    } else {
                                        textView.setText(String.valueOf(num + 1));
                                    }
                                } else {
                                    if (num - 1 == 0) {
                                        textView.setText("");
                                    } else {
                                        textView.setText(String.valueOf(num - 1));
                                    }
                                }
                            } /*else {
                                if (num >= 10000) {
                                    String s = String.valueOf(num % 10000) + "w+";
                                    textView.setText(s);
                                } else {
                                    String s = String.valueOf(num % 1000) + "k+";
                                    textView.setText(s);
                                }
                            }*/

                        } else {
                            Log.e("TVItemAty", info.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "小呗连不上网~", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 加载更多评论
     */
    @Override
    public void onLoad() {
        mItems.remove(Text.class);
        if (isLoadMoreEnable) {
            executeLoadMoreCommentTask();
        } else {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(mItems.size() - 3);
            if (viewHolder instanceof LoadMoreViewBinder.ViewHolder) {
                Log.e("TVItemAty", "true");
                LoadMoreViewBinder.ViewHolder holder = (LoadMoreViewBinder.ViewHolder) viewHolder;
                holder.textView.setText("没有更多了");
                holder.textView.setEnabled(false);
                holder.textView.setClickable(false);
            }
            mAdapter.notifyItemChanged(mItems.size() - 3);
        }
    }

    private void executeLoadMoreCommentTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(TVService.class).loadMoreComment(UserConfig.getToken(this),
                video_id, String.valueOf(mPage)).enqueue(new Callback<LoadCommentInfo>() {
            @Override
            public void onResponse(Call<LoadCommentInfo> call, Response<LoadCommentInfo> response) {
                LoadCommentInfo info = response.body();
                List<TVItemInfo.TVItemDetail.HotComment> newList = info.getData();
                if (newList != null && newList.size() > 0) {
                    int length = mAdapter.getItemCount();
                    int refreshIndex = mItems.size() - 3 - newList.size();
                    int refreshLength = 0;
                    if (mLatestComments.size() != 0) {
                        refreshIndex -= mLatestComments.size();
                        //包括最新评论的label
                        refreshIndex --;
                        refreshLength += mLatestComments.size();
                        refreshLength ++;
                    }
                    if (mHitComments.size() != 0) {
                        refreshIndex -= mHitComments.size();
                        //包括最热评论的label
                        refreshIndex --;
                        refreshLength += mHitComments.size();
                        refreshLength ++;
                    }
                    for (int i = length - 1; mAdapter.getItemCount() - 3 - refreshLength > 0; i-- ) {
                        mItems.remove(i);
                    }
                    List<TVComment> mNewComments = new ArrayList<>();
                    for (int i = 0; i < newList.size(); i++) {
                        mNewComments.add(new TVComment(newList.get(i).getSet_time(),
                                newList.get(i).getIs_like(),
                                newList.get(i).getComment(),
                                newList.get(i).getLikes(),
                                newList.get(i).getId(),
                                newList.get(i).getUser_id(),
                                newList.get(i).getPortrait(),
                                newList.get(i).getUsername()));
                    }
                    mLatestComments.addAll(mNewComments);
                    mItems.addAll(mNewComments);
                    mItems.add(new Text("加载更多评论", false));
                    mItems.add(new EmptyValue(""));
                    mItems.add(new EmptyValue(""));
                    Log.e("TVItemAty", "isenable");
                    mAdapter.notifyItemRangeChanged(refreshIndex,
                            newList.size() + 3);
                    mPage++;
                } else {
                    isLoadMoreEnable = false;
                    int length = mAdapter.getItemCount();
                    int refreshIndex = mItems.size() - 3;
                    int refreshLength = 0;
                    if (mLatestComments.size() != 0) {
                        refreshIndex -= mLatestComments.size();
                        //包括最新评论的label
                        refreshIndex --;
                        refreshLength += mLatestComments.size();
                        refreshLength ++;
                    }
                    if (mHitComments.size() != 0) {
                        refreshIndex -= mHitComments.size();
                        //包括最热评论的label
                        refreshIndex --;
                        refreshLength += mHitComments.size();
                        refreshLength ++;
                    }
                    for (int i = length - 1; mAdapter.getItemCount() - 3 - refreshLength > 0; i-- ) {
                        mItems.remove(i);
                    }
                    mItems.add(new Text("没有更多了噢", false));
                    Toast.makeText(getApplicationContext(), "没有更多了噢", Toast.LENGTH_SHORT).show();
                    mItems.add(new EmptyValue(""));
                    mItems.add(new EmptyValue(""));
                    mAdapter.notifyItemRangeChanged(refreshIndex,
                            3);
                }
            }

            @Override
            public void onFailure(Call<LoadCommentInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "连接超时了,请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
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

    private class ShareListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            Toast.makeText(TVItemAty.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(TVItemAty.this, "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(TVItemAty.this, "分享取消", Toast.LENGTH_SHORT).show();
        }
    }

}
