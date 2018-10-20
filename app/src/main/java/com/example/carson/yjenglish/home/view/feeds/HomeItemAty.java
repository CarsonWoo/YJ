package com.example.carson.yjenglish.home.view.feeds;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.FullScreenVideo;
import com.example.carson.yjenglish.ImageShowActivity;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.ExpressionAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.model.HomeItemInfo;
import com.example.carson.yjenglish.home.model.forviewbinder.Comment;
import com.example.carson.yjenglish.home.model.forviewbinder.Content;
import com.example.carson.yjenglish.home.model.forviewbinder.Recommend;
import com.example.carson.yjenglish.home.model.forviewbinder.RecommendList;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;
import com.example.carson.yjenglish.home.view.HomeFragment;
import com.example.carson.yjenglish.home.viewbinder.feeds.CommentViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.EmptyViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.ContentViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.RecommendListViewBinder;
import com.example.carson.yjenglish.home.viewbinder.feeds.VideoViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.view.users.UserInfoAty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String favourNum;
    private boolean requestComment;
    private String id;
    private String author_id;

    private ImageView expression;
    private EditText editComment;
    private InputMethodManager imm;
    private TextView send;
    private RecyclerView expressionRv;
    private ExpressionAdapter expressionAdapter;

    private boolean hasComment = false;
    private boolean isFavour, isLike;//isFavour是喜欢，isLike是点赞

    private List<HomeItemInfo.HomeItemData.Order> orders;

    private List<Comment> mHitComments = new ArrayList<>();
    private List<Comment> mLatestComments = new ArrayList<>();

    private Map<String, Integer> mLatestIndex = new HashMap<>();
    private Map<String, Integer> mHitIndex = new HashMap<>();

    private List<HomeItemInfo.HomeItemData.Comment> hitComments, newComments;
    private List<HomeItemInfo.HomeItemData.Recommendation> recommendations;

    private LinearLayout mContentLayout;

    private String commentId;
    private boolean isSendSubComment = false;

    private Dialog commentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
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
//        likeNum = fromData.getStringExtra("like_num");
        requestComment = fromData.getBooleanExtra("request_comment", false);
//        isFavour = fromData.getBooleanExtra("is_like", false);

        commentDialog = DialogUtils.getInstance(this).newCommonDialog("正在上传评论中,请稍候...",
                R.mipmap.gif_loading_video, true);
        commentDialog.setCanceledOnTouchOutside(false);

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

                    author_id = response.body().getData().getUser_id();

                    recommendations = info.getData().getRecommendations();
                    hitComments = info.getData().getHot_comment();
                    newComments = info.getData().getNew_comment();

                    isFavour = info.getData().getIs_favour().equals("1");
                    like.setSelected(isFavour);

                    favourNum = info.getData().getFavours();

                    isLike = info.getData().getIs_like().equals("1");

                    Log.e("HomeItemAty", "isLike = " + isLike);

                    likeNum = info.getData().getLikes();

                    if (hitComments.size() == 0 && newComments.size() == 0) {
                        hasComment = false;
                    } else {
                        hasComment = true;
                    }

                    if (videoUrl == null || videoUrl.isEmpty()) {
                        videoUrl = info.getData().getVideo();
                    }

                    if (imgUrl == null || imgUrl.isEmpty()) {
                        imgUrl = info.getData().getPic();
                    }

                    if (username == null || username.isEmpty()) {
                        username = info.getData().getAuthor_username();
                    }

                    if (portraitUrl == null || portraitUrl.isEmpty()) {
                        portraitUrl = info.getData().getAuthor_portrait();
                    }

                    if (title == null || title.isEmpty()) {
                        title = info.getData().getTitle();
                    }

                    loadCommentList(info.getData().getUser_id());

                    initViews();
                }
            }

            @Override
            public void onFailure(Call<HomeItemInfo> call, Throwable t) {
                Log.e("HomeItemAty", "连接超时");
                Toast.makeText(getApplicationContext(), "网络开小差了,正在努力重试~", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeLoadTask();
                    }
                }, 5000);
            }
        });
    }

    private void loadCommentList(String user_id) {
        mHitComments.clear();
        mLatestComments.clear();

        for (int i = 0; i < hitComments.size(); i++) {
            int index = -1;
            for (int j = 0; j < hitComments.get(i).getInner_comment().size(); j++) {
                if (hitComments.get(i).getInner_comment().get(j).getUser_id().equals(author_id)) {
                    index = j;
                    break;
                }
            }
            mHitComments.add(new Comment(hitComments.get(i).getUser_id(), hitComments.get(i).getUsername(), hitComments.get(i).getPortrait(),
                    hitComments.get(i).getSet_time(), hitComments.get(i).getComment(),
                    (index == -1 || hitComments.get(i).getInner_comment().size() == 0) ?
                            null : hitComments.get(i).getInner_comment().get(index).getComment(),
                    Integer.parseInt(hitComments.get(i).getLikes()),
                    hitComments.get(i).getInner_comment().size() == 0 ? null :
                            new Comment.Reply(hitComments.get(i).getInner_comment().get(0).getUser_id(),
                                    hitComments.get(i).getInner_comment().get(0).getUsername(),
                                    hitComments.get(i).getInner_comment().get(0).getPortrait(),
                                    hitComments.get(i).getInner_comment().get(0).getSet_time(),
                                    hitComments.get(i).getInner_comment().get(0).getComment(),
                                    Integer.parseInt(hitComments.get(i).getInner_comment().get(0).getLikes()),
                                    /*Integer.parseInt(hitComments.get(i).getInner_comment().get(0))*/
                                    hitComments.get(i).getInner_comment().get(0).getId(),
                                    hitComments.get(i).getInner_comment().get(0).getIs_like().equals("1")),
                    hitComments.get(i).getId(), hitComments.get(i).getIs_like().equals("1")));
        }
        for (int i = 0; i < newComments.size(); i++) {
            int index = -1;
            for (int j = 0; j < newComments.get(i).getInner_comment().size(); j++) {
                if (newComments.get(i).getInner_comment().get(j).getUser_id().equals(user_id)) {
                    index = j;
                    break;
                }
            }
            mLatestComments.add(new Comment(newComments.get(i).getUser_id(), newComments.get(i).getUsername(), newComments.get(i).getPortrait(),
                    newComments.get(i).getSet_time(), newComments.get(i).getComment(),
                    (index == -1 || newComments.get(i).getInner_comment().size() == 0) ?
                            null : newComments.get(i).getInner_comment().get(index).getComment(),
                    Integer.parseInt(newComments.get(i).getLikes()),
                    newComments.get(i).getInner_comment().size() == 0 ? null :
                            new Comment.Reply(newComments.get(i).getInner_comment().get(0).getUser_id(),
                                    newComments.get(i).getInner_comment().get(0).getUsername(),
                                    newComments.get(i).getInner_comment().get(0).getPortrait(),
                                    newComments.get(i).getInner_comment().get(0).getSet_time(),
                                    newComments.get(i).getInner_comment().get(0).getComment(),
                                    Integer.parseInt(newComments.get(i).getInner_comment().get(0).getLikes()),
                                    newComments.get(i).getInner_comment().get(i).getId(),
                                    newComments.get(i).getInner_comment().get(0).getIs_like().equals("1")),
                    newComments.get(i).getId(), newComments.get(i).getIs_like().equals("1")));
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
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toImg = new Intent(HomeItemAty.this, ImageShowActivity.class);
                        toImg.putExtra("img_url", order.getPic());
                        startActivity(toImg);
                    }
                });
            } else {
                String s = order.getParagraph();
                TextView textView = new TextView(HomeItemAty.this);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                textView.setText(s);
                mContentLayout.addView(textView);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, ScreenUtils.dp2px(this, 15),
                        0, ScreenUtils.dp2px(this, 15));
                textView.setLayoutParams(params);
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

//        like.setSelected(isFavour);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeFavourTask();
            }
        });

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

    private void executeFavourTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).postFavours(UserConfig.getToken(this), id)
                .enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        if (response.body().getStatus().equals("200")) {
                            isFavour = !isFavour;
                            like.setSelected(isFavour);
                            Intent backIntent = new Intent();
                            backIntent.putExtra("favour_change", isFavour ? "1" : "0");
                            setResult(HomeFragment.RESULT_LIKE_CHANGE, backIntent);
                        } else {
                            Log.e("HomeItemAty", response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {
                        Toast.makeText(HomeItemAty.this, "连接超时", Toast.LENGTH_SHORT).show();
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
                    /**
                     * 到时需要改 将2000改为前四个item的高度总和
                     */
                    if (mDistance < 2000) {
                        recyclerView.smoothScrollToPosition(4);
                    }
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
            hasComment = true;
        }
        commentDialog.show();
        WindowManager.LayoutParams lp = commentDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 240);
        lp.gravity = Gravity.CENTER;
        commentDialog.getWindow().setAttributes(lp);
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).sendComment(UserConfig.getToken(this), id,
                editComment.getText().toString()).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                if (response.body().getStatus().equals("200")) {
//                    initComment(true);
                    refreshCommentList();
                } else {
                    Log.e("HomeItemAty", response.body().getMsg());
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
                            commentDialog.dismiss();
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
                isSendSubComment = false;
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
        mItems.add(new Content(author_id, title, portraitUrl, username, likeNum));

        if (recommendations.size() > 0) {
            mItems.add(new EmptyValue("热门推荐"));
            List<Recommend> recommendList = new ArrayList<>();
            for (int i = 0; i < recommendations.size(); i++) {
                if (recommendations.get(i).getKind() == null) {
                    recommendations.get(i).setKind("");
                }
                Recommend recommend = new Recommend(recommendations.get(i).getId(),
                        recommendations.get(i).getTitle(), recommendations.get(i).getPic(),
                        recommendations.get(i).getVideo(),
                        recommendations.get(i).getLikes(),
                        recommendations.get(i).getAuthor_portrait(), recommendations.get(i).getAuthor_username(),
                        "#" + recommendations.get(i).getKind());
                recommendList.add(recommend);
            }
            mItems.add(new RecommendList(recommendList));
        }

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
                    mVideoView.resume();
                }
                isFullClick = false;
            }
        }
    }

    /**
     * 此方法为文章赞赏的回调
     */
    @Override
    public void onLikeClick(final TextView tv, final String likes) {

        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        HomeService service = retrofit.create(HomeService.class);
        service.postLikes(UserConfig.getToken(this), id).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                if (response.body().getStatus().equals("200")) {
                    int num;
                    if (tv.getText().toString().equals("") || tv.getText().toString().isEmpty()) {
                        num = 0;
                    } else {
                        num = Integer.parseInt(tv.getText().toString());
                    }
                    if (isLike) {
                        num --;
                        if (num == 0) {
                            tv.setText("");
                        } else {
                            tv.setText(String.valueOf(num));
                        }
                    } else {
                        num ++;
                        tv.setText(String.valueOf(num));
                    }
                    isLike = !isLike;
                } else {
                    Log.e("HomeItemAty", response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Toast.makeText(HomeItemAty.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 以下三个方法为Comment的回调
     */
    @Override
    public void onLoadMoreReply(String user_id, String comment_id) {
        Intent toComment = new Intent(this, CommentAty.class);
        toComment.putExtra("id", id);
        toComment.putExtra("user_id", user_id);
        toComment.putExtra("comment_id", comment_id);
        startActivity(toComment);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    @Override
    public void onReply(String username, int pos, String comment_id) {
        imm.showSoftInput(editComment, InputMethodManager.SHOW_FORCED);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        editComment.setHint("回复@" + username + ":");
        editComment.setFocusable(true);
        editComment.setFocusableInTouchMode(true);
        editComment.requestFocus();
        editComment.findFocus();
        commentId = comment_id;
        isSendSubComment = true;
    }

    //评论的点赞
    @Override
    public void onLikeButtonClick(final TextView tv, String comment_id, boolean isReply, final boolean isLike) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).postCommentLike(UserConfig.getToken(this),
                comment_id).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    int num;
                    if (tv.getText().toString().equals("") || tv.getText().toString().isEmpty()) {
                        num = 0;
                    } else {
                        if (tv.getText().toString().endsWith("+")) {
                            num = 10000;
                        } else {
                            num = Integer.parseInt(tv.getText().toString());
                        }
                    }
                    if (num < 1000) {
                        if (isLike) {
                            if (num - 1 == 0) {
                                tv.setText("");
                            } else {
                                tv.setText(String.valueOf(num - 1));
                            }
                        } else {
                            if (num + 1 == 1000) {
                                tv.setText("1k+");
                            } else {
                                tv.setText(String.valueOf(num + 1));
                            }
                        }
                    } /*else {
                        if (num > 10000) {
                            String s = String.valueOf(num % 10000) + "w+";
                            tv.setText(s);
                        } else {
                            String s = String.valueOf(num % 1000) + "k+";
                            tv.setText(s);
                        }
                    }*/
                } else {
                    Log.e("HomeItemAty", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Toast.makeText(HomeItemAty.this, "连接超时, 请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSubLikeButtonClick(final TextView tv, String comment_id, boolean isReply, final boolean isLike) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).postSubCommentLike(UserConfig.getToken(this),
                comment_id).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    int num;
                    if (tv.getText().toString().equals("") || tv.getText().toString().isEmpty()) {
                        num = 0;
                    } else {
                        if (tv.getText().toString().endsWith("+")) {
                            num = 10000;
                        } else {
                            num = Integer.parseInt(tv.getText().toString());
                        }
                    }
                    if (num < 1000) {
                        if (isLike) {
                            if (num - 1 == 0) {
                                tv.setText("");
                            } else {
                                tv.setText(String.valueOf(num - 1));
                            }
                        } else {
                            if (num + 1 == 1000) {
                                tv.setText("1k+");
                            } else {
                                tv.setText(String.valueOf(num + 1));
                            }
                        }
                    } /*else {
                        if (num > 10000) {
                            String s = String.valueOf(num % 10000) + "w+";
                            tv.setText(s);
                        } else {
                            String s = String.valueOf(num % 1000) + "k+";
                            tv.setText(s);
                        }
                    }*/
                } else {
                    Log.e("HomeItemAty", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Toast.makeText(HomeItemAty.this, "连接超时, 请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendSubComment() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).sendSubComments(UserConfig.getToken(this),
                commentId, editComment.getText().toString()).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    refreshCommentList();
                } else {
                    Log.e("HomeItemAty", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Toast.makeText(HomeItemAty.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUserPortraitClick(String user_id) {
        Intent toUserInfo = new Intent(this, UserInfoAty.class);
        toUserInfo.putExtra("user_id", user_id);
        startActivity(toUserInfo);
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(final Editable editable) {
            if (!TextUtils.isEmpty(editable)) {
                send.setEnabled(true);
                ViewGroup.LayoutParams lp = editComment.getLayoutParams();
                lp.width = ScreenUtils.dp2px(HomeItemAty.this, 290);
                editComment.setLayoutParams(lp);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editComment.getHint().toString().startsWith("回复@")) {
                            Log.e("HomeItemAty", "sendSubComment");
                            sendSubComment();
                        } else {
                            doSendWork();
                        }
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.release();
        }
    }
}
