package com.example.carson.yjenglish.home.view.feeds;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.CommentAdapter;
import com.example.carson.yjenglish.adapter.ExpressionAdapter;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.model.CommentDetailInfo;
import com.example.carson.yjenglish.home.model.HomeItemInfo;
import com.example.carson.yjenglish.home.model.forviewbinder.Comment;
import com.example.carson.yjenglish.msg.view.ReportActivity;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.view.users.UserInfoAty;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Header;

public class CommentAty extends AppCompatActivity implements View.OnClickListener, CommentAdapter.OnSelectItemListener {

    private ImageView back;
    private TextView title;
    private CircleImageView portrait;
    private TextView username;
    private TextView likeNum;
    private TextView time;
    private TextView comment;
    private ImageView btnLike;
    private ImageView menuMore;
    private TextView sort;
    private PullToRefreshRecyclerView rvReply;
    private EditText editComment;
    private ImageView expression;
    private RecyclerView expressionRv;

    private ExpressionAdapter expressionAdapter;
    private TextView send;

    private List<Comment> mReplies = new ArrayList<>();

    private CommentAdapter commentAdapter;
    private int mLastPosition = 0;

    private InputMethodManager imm;

    private String id;

    private String user_id;

    private String comment_id;

    private boolean isLike;

    private boolean isRefresh = false;

    private List<HomeItemInfo.HomeItemData.Comment.InnerComment> comments;

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
        setContentView(R.layout.activity_comment);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        id = getIntent().getStringExtra("id");
        user_id = getIntent().getStringExtra("user_id");
        comment_id = getIntent().getStringExtra("comment_id");
//        initViews();
        bindViews();
        executeLoadTask();
    }

    private void executeLoadTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).getCommentDetail(UserConfig.getToken(this),
                comment_id).enqueue(new Callback<CommentDetailInfo>() {
            @Override
            public void onResponse(Call<CommentDetailInfo> call, Response<CommentDetailInfo> response) {
                CommentDetailInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    if (!isRefresh) {
                        comments = info.getData().getInner_comment();
                        title.setText(info.getData().getComments() + "条回复");
                        Glide.with(CommentAty.this)
                                .load(info.getData().getPortrait())
                                .into(portrait);
                        username.setText(info.getData().getUsername());
                        likeNum.setText(info.getData().getLikes());
                        time.setText(info.getData().getSet_time());
                        comment.setText(info.getData().getComment());
                        btnLike.setSelected(info.getData().getIs_like().equals("1"));

                        isLike = info.getData().getIs_like().equals("1");
                        initRecycler();
                    } else {
                        mReplies.clear();
                        comments = info.getData().getInner_comment();
                        title.setText(info.getData().getComments() + "条回复");
                        for (int i = 0; i < comments.size(); i++) {
                            mReplies.add(new Comment(comments.get(i).getUser_id(), comments.get(i).getUsername(),
                                    comments.get(i).getPortrait(), comments.get(i).getSet_time(),
                                    comments.get(i).getComment(), null, Integer.parseInt(comments.get(i).getLikes()), null,
                                    comments.get(i).getId(), false/*comments.get(i).getIs_like().equals("1")*/));
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<CommentDetailInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "网络开小差咯...正在重试", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeLoadTask();
                    }
                }, 3000);
            }
        });
    }

    private void bindViews() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.comment_title);
        portrait = findViewById(R.id.portrait);
        username = findViewById(R.id.username);
        likeNum = findViewById(R.id.like_num);
        time = findViewById(R.id.time);
        comment = findViewById(R.id.comment);
        btnLike = findViewById(R.id.like_btn);
        menuMore = findViewById(R.id.menu_more);
        sort = findViewById(R.id.sort);
        rvReply = findViewById(R.id.rv_comment);
        editComment = findViewById(R.id.edit_comment);
        expression = findViewById(R.id.expression);
        expressionRv = findViewById(R.id.expression_recycler);
        send = findViewById(R.id.send);

//        initData();
        initEditText();

        back.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        menuMore.setOnClickListener(this);
        sort.setOnClickListener(this);

        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentAty.this, UserInfoAty.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
            }
        });
    }

    private void initRecycler() {
        rvReply.setLayoutManager(new LinearLayoutManager(this));
        rvReply.setPullRefreshEnabled(false);
        rvReply.setLoadingMoreEnabled(false);
        for (int i = 0; i < comments.size(); i++) {
            mReplies.add(new Comment(comments.get(i).getUser_id(), comments.get(i).getUsername(),
                    comments.get(i).getPortrait(), comments.get(i).getSet_time(),
                    comments.get(i).getComment(), null, Integer.parseInt(comments.get(i).getLikes()), null,
                    comments.get(i).getId(), false/*comments.get(i).getIs_like().equals("1")*/));
        }
        commentAdapter = new CommentAdapter(this, mReplies);
        rvReply.setAdapter(commentAdapter);
//        rvReply.setItemAnimator(new DefaultItemAnimator());
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
                expressionRv.setLayoutManager(new GridLayoutManager(CommentAty.this, 5));
                expressionAdapter = new ExpressionAdapter(CommentAty.this);
                expressionRv.setAdapter(expressionAdapter);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.like_btn:
                doLikeWork();
                break;
            case R.id.menu_more:
                doMenuWork();
                break;
            case R.id.sort:
                doSortWork();
                break;
            case R.id.back:
                onBackPressed();
                break;
            default:
                break;

        }
    }

    private void doSortWork() {
        DialogUtils du = DialogUtils.getInstance(this);
        Dialog mDialog = du.newSortDialog(sort);
        du.setSortListener(new DialogUtils.OnSortListener() {
            @Override
            public void onItemSelected(int pos) {
                if (pos == 0) {
                    //按时间排序
                } else {
                    //按热度排序
                }
            }
        });
        du.show(mDialog);
    }

    private void doMenuWork() {
        View windowView = LayoutInflater.from(this).inflate(R.layout.layout_common_dialog, null, false);
        ImageView img = windowView.findViewById(R.id.common_img);
        TextView text = windowView.findViewById(R.id.common_text);
        CardView card = windowView.findViewById(R.id.card_view);
        img.setVisibility(View.GONE);
        text.setTextSize(13);
        text.setTextColor(getResources().getColor(R.color.colorTextWord));
        text.setText("举报评论");
        card.setRadius(6f);
        final PopupWindow window = new PopupWindow(windowView, ScreenUtils.dp2px(this, 180),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.showAsDropDown(menuMore, 0, -window.getHeight(), Gravity.BOTTOM | Gravity.END);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toReport = new Intent(CommentAty.this, ReportActivity.class);
                toReport.putExtra("comment_id", comment_id);
                startActivity(toReport);
                window.dismiss();
            }
        });
    }

    private void doLikeWork() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).postCommentLike(UserConfig.getToken(this), comment_id)
                .enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            int num;
                            if (likeNum.getText().toString().equals("0")) {
                                num = 0;
                            } else {
                                num = Integer.parseInt(likeNum.getText().toString());
                            }
                            if (isLike) {
                                isLike = !isLike;
                                btnLike.setSelected(isLike);
                                ObjectAnimator animatorX = ObjectAnimator.ofFloat(btnLike,
                                        "scaleX", 1.3f, 1.0f);
                                ObjectAnimator animatorY = ObjectAnimator.ofFloat(btnLike,
                                        "scaleY", 1.3f, 1.0f);
                                AnimatorSet set = new AnimatorSet();
                                set.play(animatorX).with(animatorY);
                                set.setDuration(400).start();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onLoadMoreReply() {

    }

    @Override
    public void onReply(String username, int pos) {

    }

    @Override
    public void onLikeButtonClick(String comment_id, final TextView textView, final boolean is_like) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).postSubCommentLike(UserConfig.getToken(this),
                comment_id).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    int num;
                    if (textView.getText().toString().isEmpty()) {
                        num = 0;
                    } else {
                        num = Integer.parseInt(textView.getText().toString());
                    }
                    if (is_like) {
                        textView.setText(String.valueOf(num + 1));
                    } else {
                        textView.setText(String.valueOf(num - 1));
                    }
                } else {
                    Toast.makeText(CommentAty.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {

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
                lp.width = ScreenUtils.dp2px(CommentAty.this, 290);
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

    private void doSendWork() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).sendSubComments(UserConfig.getToken(this),
                id, editComment.getText().toString()).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    editComment.setText("");
                    isRefresh = true;
                    executeLoadTask();
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
