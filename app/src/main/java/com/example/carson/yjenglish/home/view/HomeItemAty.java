package com.example.carson.yjenglish.home.view;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
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
import com.example.carson.yjenglish.adapter.ExpressionAdapter;
import com.example.carson.yjenglish.customviews.MyVideoView;
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
import com.example.carson.yjenglish.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

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
    private int likeNum;
    private boolean requestComment;

    private ImageView expression;
    private EditText editComment;
    private InputMethodManager imm;
    private TextView send;
    private RecyclerView expressionRv;
    private ExpressionAdapter expressionAdapter;

    private boolean hasComment = false;

    private List<Comment> mHitComments = new ArrayList<>();
    private List<Comment> mLatestComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_item);
        //读取数据
        Intent fromData = getIntent();
        videoUrl = fromData.getStringExtra("video_url");
        imgUrl = fromData.getStringExtra("img_url");
        username = fromData.getStringExtra("username");
        portraitUrl = fromData.getStringExtra("portrait_url");
        title = fromData.getStringExtra("title");
        likeNum = fromData.getIntExtra("like_num", 0);
        requestComment = fromData.getBooleanExtra("request_comment", false);

        for (int i = 0; i < 3; i++) {
            if (i == 1) {
                mHitComments.add(new Comment("Carson", "http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg",
                        "今天13:01", "I love this style.", "您的支持是我们最宝贵的财富", 666, new Comment.Reply("Lexie",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg",
                        "刚刚", "哎呦不错噢", 20)));
            } else if (i == 2) {
                mHitComments.add(new Comment("Nelson", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg",
                        "刚刚", "好文章！", "谢谢您的支持", 10, null));
            } else {
                mHitComments.add(new Comment("Nelson", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg",
                        "刚刚", "好文章！", null, 10, null));
            }
        }

        for (int i = 0; i < 3; i++) {
            if (i % 2 == 0) {
                mLatestComments.add(new Comment("Tiffany",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2648413720,1172680468&fm=27&gp=0.jpg",
                        "今天11:08", "我觉得OK", null, 0, null));
            } else {
                mLatestComments.add(new Comment("帅的被人砍",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg",
                        "刚刚", "我觉得不行", null, 0, null));
            }
        }
        initViews();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
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
            mItems.remove(4);
            hasComment = true;
            mLatestComments.add(0, new Comment(username, portraitUrl, "刚刚",
                    editComment.getText().toString(), null, 0, null));
            initComment(true);
        } else {
            if (editComment.getHint().toString().startsWith("回复")) {
                Log.e("HomeItemAty", "回复了");
                editComment.setHint("发表评论");
            } else {
                mLatestComments.add(0, new Comment(username, portraitUrl, "刚刚",
                        editComment.getText().toString(), null, 0, null));
                mItems.removeAll(mLatestComments);
                for (int i = 0; i < 2; i++) {
                    mItems.remove(mItems.size() - 1);
                }
                for (int i = 0; i < 3; i++) {
                    mItems.add(mLatestComments.get(i));
                }
                mItems.add(new EmptyValue(""));
                mItems.add(new EmptyValue(""));
            }

        }
        //TODO 届时需联网
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
        }
        editComment.setFocusable(false);
        expressionRv.setVisibility(View.GONE);
        editComment.setText("");
        mAdapter.notifyDataSetChanged();
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
                float percent = mDistance * 1f / maxDistance;
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
        mAdapter.register(Content.class, new ContentViewBinder(this));
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
        mItems.add(new Video(imgUrl, videoUrl));
        mItems.add(new Content(title, portraitUrl, username, "content", likeNum));
        mItems.add(new EmptyValue("热门推荐"));

        List<Recommend> recommendList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Recommend recommend = new Recommend("原来学英语这么简单", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg",
                    "http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg", "Moon",
                    "#记忆方法");
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
        } else {
            firstTimeInitComment();
        }

    }

    private void firstTimeInitComment() {
        mItems.add(new EmptyValue("热门评论"));
        for (int i = 0 ; i < 3; i++) {
            mItems.add(mHitComments.get(i));
        }
        mItems.add(new EmptyValue("最新评论"));
        for (int i = 0; i < 3; i++) {
            mItems.add(mLatestComments.get(i));
        }
    }

    private void setToolbarAlpha(int alpha) {
        if (alpha >= 125) {
            alpha = 125;
        } else {
            mToolbar.getBackground().setAlpha(alpha);
        }
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
