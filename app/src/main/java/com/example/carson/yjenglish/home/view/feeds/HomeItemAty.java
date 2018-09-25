package com.example.carson.yjenglish.home.view.feeds;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.FullScreenVideo;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.ExpressionAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.model.HomeItem;
import com.example.carson.yjenglish.home.model.HomeItemInfo;
import com.example.carson.yjenglish.home.model.forviewbinder.Comment;
import com.example.carson.yjenglish.home.model.forviewbinder.Content;
import com.example.carson.yjenglish.home.model.forviewbinder.Recommend;
import com.example.carson.yjenglish.home.model.forviewbinder.RecommendList;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;
import com.example.carson.yjenglish.home.viewbinder.feeds.CommentViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.EmptyViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.ContentViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.RecommendListViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.VideoViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeItemAty extends AppCompatActivity implements VideoViewBinder.OnVideoClickListener,
        ContentViewBinder.OnLikeFabClickListener, CommentViewBinder.OnItemSelectListener{

    private ConstraintLayout mRoot;
    private RelativeLayout mToolbar;
    private ImageView back;
    private ImageView like;
    private ImageView share;

    private RecyclerView recyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;

    private MyVideoView mVideoView;

    private View lastView;

    private boolean isFullClick = false;
    private int mDistance = 0;
    private int maxDistance = 255;//当距离在[0,255]变化时，透明度在[0,255]之间变化

    private String videoUrl;
    private String imgUrl;
    private String username;
    private String portraitUrl;
    private String title;
    private String likeNum;
    private boolean requestComment;
    private String id;

    private ImageView expression;
    private EditText editComment;
    private InputMethodManager imm;
    private TextView send;
    private RecyclerView expressionRv;
    private ExpressionAdapter expressionAdapter;

    private boolean hasComment = false;
    private boolean isLike;

    private List<HomeItemInfo.HomeItemData.Order> orders;
    private SpannableStringBuilder mContent;

    private List<Comment> mHitComments = new ArrayList<>();
    private List<Comment> mLatestComments = new ArrayList<>();

    private List<HomeItemInfo.HomeItemData.Comment> hitComments, newComments;
    private List<HomeItemInfo.HomeItemData.Recommendation> recommendations;

    private LinearLayout mContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_item);

        mContentLayout = new LinearLayout(this);
        mContentLayout.setOrientation(LinearLayout.VERTICAL);
        mContentLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        //读取数据
        Intent fromData = getIntent();
        id = fromData.getStringExtra("id");
        videoUrl = fromData.getStringExtra("video_url");
        imgUrl = fromData.getStringExtra("img_url");
        username = fromData.getStringExtra("username");
        portraitUrl = fromData.getStringExtra("portrait_url");
        title = fromData.getStringExtra("title");
        likeNum = fromData.getStringExtra("like_num");
        requestComment = fromData.getBooleanExtra("request_comment", false);
        isLike = fromData.getBooleanExtra("is_like", false);

        bindViews();
        executeLoadTask();
//        initViews();
    }

    private void executeLoadTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        HomeService service = retrofit.create(HomeService.class);
        service.getHomeItemInfo(UserConfig.getToken(this), id).enqueue(new Callback<HomeItemInfo>() {
            @Override
            public void onResponse(Call<HomeItemInfo> call, Response<HomeItemInfo> response) {
                HomeItemInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    orders = info.getData().getOrder();
                    initContent();

                    recommendations = info.getData().getRecommendations();
                    hitComments = info.getData().getHot_comment();
                    newComments = info.getData().getNew_comment();

                    if (hitComments.size() == 0 && newComments.size() == 0) {
                        hasComment = false;
                    } else {
                        hasComment = true;
                    }

                    loadCommentList(info.getData().getUser_id());

                    initViews();
                }
            }

            @Override
            public void onFailure(Call<HomeItemInfo> call, Throwable t) {
                Log.e("HomeItemAty", "连接超时");
            }
        });
    }

    private void loadCommentList(String user_id) {
        mHitComments.clear();
        mLatestComments.clear();
        for (int i = 0; i < hitComments.size(); i++) {
            mHitComments.add(new Comment(hitComments.get(i).getUsername(), hitComments.get(i).getPortrait(),
                    hitComments.get(i).getSet_time(), hitComments.get(i).getComment(),
                    hitComments.get(i).getInner_comment().size() == 0 ? null :
                            hitComments.get(i).getInner_comment().get(Integer.parseInt(user_id) - 1).getComment(),
                    Integer.parseInt(hitComments.get(i).getLikes()),
                    hitComments.get(i).getInner_comment().size() == 0 ? null :
                            new Comment.Reply(hitComments.get(0).getUsername(),
                                    hitComments.get(0).getPortrait(), hitComments.get(0).getSet_time(),
                                    hitComments.get(0).getComment(),
                                    Integer.parseInt(hitComments.get(0).getLikes()))
            ));
        }
        for (int i = 0; i < newComments.size(); i++) {
            mLatestComments.add(new Comment(newComments.get(i).getUsername(), newComments.get(i).getPortrait(),
                    newComments.get(i).getSet_time(), newComments.get(i).getComment(),
                    newComments.get(i).getInner_comment().size() == 0 ? null :
                            newComments.get(i).getInner_comment().get(Integer.parseInt(user_id) - 1).getComment(),
                    Integer.parseInt(newComments.get(i).getLikes()),
                    newComments.get(i).getInner_comment().size() == 0 ? null :
                            new Comment.Reply(newComments.get(0).getUsername(),
                                    newComments.get(0).getPortrait(), newComments.get(0).getSet_time(),
                                    newComments.get(0).getComment(),
                                    Integer.parseInt(newComments.get(0).getLikes()))
            ));
        }
    }

    private void initContent() {
        for (final HomeItemInfo.HomeItemData.Order order : orders) {
            if (order.getPic() != null) {
                final ImageView imageView = new ImageView(HomeItemAty.this);
                mContentLayout.addView(imageView);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                imageView.setLayoutParams(params);
                /* 将图片宽度设为MATCH_PARENT 并使高度自适配 */
                Glide.with(HomeItemAty.this).load(order.getPic())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                                int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();

                                //获取比例
                                float scale = (float) vw / (float) resource.getIntrinsicWidth();

                                int vh = Math.round(resource.getIntrinsicHeight() * scale);
                                lp.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();

                                imageView.setLayoutParams(lp);
                                return false;
                            }
                        })
                        .thumbnail(0.5f).into(imageView);
            } else {
                String s = order.getParagraph();
                TextView textView = new TextView(HomeItemAty.this);
                textView.setText(s + "\n");
                mContentLayout.addView(textView);
                ViewGroup.LayoutParams params = textView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
    }

    private void bindViews() {
        mRoot = findViewById(R.id.root_view);
        mToolbar = findViewById(R.id.toolbar);
        setToolbarAlpha(0);
        back = findViewById(R.id.back);
        like = findViewById(R.id.like);
        share = findViewById(R.id.share);
        recyclerView = findViewById(R.id.recycler_view);
        expression = findViewById(R.id.expression);
        editComment = findViewById(R.id.edit_comment);
        send = findViewById(R.id.send);
        expressionRv = findViewById(R.id.expression_recycler);

        like.setSelected(isLike);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                if (mVideoView != null) {
                    mVideoView.stop();
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        Log.e("HomeItemAty", "initViews");
        initEditText();
        initRecyclerViews();
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
                    recyclerView.smoothScrollToPosition(4);
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
                expressionRv.setLayoutManager(new GridLayoutManager(HomeItemAty.this, 5));
                expressionAdapter = new ExpressionAdapter(HomeItemAty.this);
                expressionRv.setAdapter(expressionAdapter);
            }
        });
    }

    private void doSendWork() {
        if (!hasComment) {
            //将原本没有评论的emptyView去除
            mItems.remove(4);
        }
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).sendComment(UserConfig.getToken(this), id,
                editComment.getText().toString()).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                if (response.body().getStatus().equals("200")) {
//                    initComment(true);
                    refreshCommentList();
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Toast.makeText(HomeItemAty.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
        }
        editComment.setFocusable(false);
        expressionRv.setVisibility(View.GONE);

    }

    private void refreshCommentList() {
        NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST).create(HomeService.class)
                .getHomeItemInfo(UserConfig.getToken(this), id)
                .enqueue(new Callback<HomeItemInfo>() {
                    @Override
                    public void onResponse(Call<HomeItemInfo> call, Response<HomeItemInfo> response) {
                        HomeItemInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            hitComments.clear();
                            newComments.clear();
                            hitComments.addAll(info.getData().getHot_comment());
                            newComments.addAll(info.getData().getNew_comment());

                            loadCommentList(info.getData().getUser_id());

                            editComment.setText("");
                            initComment(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<HomeItemInfo> call, Throwable t) {
                        Toast.makeText(HomeItemAty.this, "连接超时", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initRecyclerViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
        });

        mAdapter.register(Video.class, new VideoViewBinder(this));
        /**
         * 需要改ContentViewBinder
         */
        mAdapter.register(Content.class, new ContentViewBinder(this, mContentLayout));
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
        mAdapter.register(RecommendList.class, new RecommendListViewBinder());
        mAdapter.register(Comment.class, new CommentViewBinder(this));
        mItems.add(new Video("", imgUrl, videoUrl));
        mItems.add(new Content(title, portraitUrl, username, mContent, likeNum));
        mItems.add(new EmptyValue("热门推荐"));

        List<Recommend> recommendList = new ArrayList<>();
        for (int i = 0; i < recommendations.size(); i++) {
            if (recommendations.get(i).getKind() == null) {
                recommendations.get(i).setKind("");
            }
            Recommend recommend = new Recommend(recommendations.get(i).getTitle(), recommendations.get(i).getPic(),
                    recommendations.get(i).getAuthor_portrait(), recommendations.get(i).getAuthor_username(),
                    "#" + recommendations.get(i).getKind());
            recommendList.add(recommend);
        }
        mItems.add(new RecommendList(recommendList));

        if (hasComment) {
            initComment(false);
        } else {
            mItems.add(new EmptyValue("还没有评论，快来抢沙发！", R.mipmap.bg_plan_box));
        }

        mItems.add(new EmptyValue(""));
        mItems.add(new EmptyValue(""));

        mAdapter.notifyDataSetChanged();


    }

    private void initComment(boolean isRefresh) {
        if (isRefresh) {
            mItems.removeAll(mHitComments);
            mItems.removeAll(mLatestComments);
            int length = mAdapter.getItemCount();
            for (int i = length - 1; mAdapter.getItemCount() - 4 > 0; i-- ) {
                mItems.remove(i);
            }
            firstTimeInitComment();
            mItems.add(new EmptyValue(""));
            mItems.add(new EmptyValue(""));
            mAdapter.notifyItemRangeChanged(4, mItems.size() - 4);
        } else {
            firstTimeInitComment();
        }

    }

    private void firstTimeInitComment() {
        if (hitComments.size() != 0) {
            mItems.add(new EmptyValue("热门评论"));
            mItems.addAll(mHitComments);
        }
        if (newComments.size() != 0) {
            mItems.add(new EmptyValue("最新评论"));
            mItems.addAll(mLatestComments);
        }
    }

    private void setToolbarAlpha(int alpha) {
        if (alpha >= 255) {
            alpha = 255;
        }
        mToolbar.getBackground().setAlpha(alpha);

    }

    private void showVideo(View view, final String path) {
        View v;
        removeVideoView();
        if (mVideoView == null) {
            mVideoView = new MyVideoView(this);
        }
        mVideoView.stop();
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
                Intent toFullScreen = new Intent(HomeItemAty.this, FullScreenVideo.class);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onVideoClick(View view, String url) {
        showVideo(view, url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    /**
     * 此方法为文章赞赏的回调
     */
    @Override
    public void onLikeClick() {
        //TODO post to server
    }

    /**
     * 以下三个方法为Comment的回调
     */
    @Override
    public void onLoadMoreReply() {
        Intent toComment = new Intent(this, CommentAty.class);
        startActivity(toComment);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    @Override
    public void onReply(String username, int pos) {
        imm.showSoftInput(editComment, InputMethodManager.SHOW_FORCED);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        editComment.setHint("回复@" + username + ":");
        editComment.setFocusable(true);
        editComment.setFocusableInTouchMode(true);
        editComment.requestFocus();
        editComment.findFocus();
    }

    @Override
    public void onLikeButtonClick(boolean isReply) {
        //TODO 联网
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
                lp.width = ScreenUtils.dp2px(HomeItemAty.this, 290);
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
