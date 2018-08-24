package com.example.carson.yjenglish.home.view;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import com.example.carson.yjenglish.adapter.MsgAdapters;
import com.example.carson.yjenglish.home.model.forviewbinder.Comment;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAty extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        initViews();
    }

    private void initViews() {
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

        initData();
        back.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        menuMore.setOnClickListener(this);
        sort.setOnClickListener(this);
        initEditText();
        initRecycler();
    }

    private void initRecycler() {
        rvReply.setLayoutManager(new LinearLayoutManager(this));
        rvReply.setPullRefreshEnabled(false);
        rvReply.setLoadingMoreEnabled(true);
        rvReply.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (mReplies.size() > 20) {
                    Toast.makeText(CommentAty.this, "加载到底了", Toast.LENGTH_SHORT).show();
                    rvReply.setLoadingMoreEnabled(false);
                    rvReply.setLoadMoreResource(R.drawable.ic_like_pink_fill);
                } else {
                    for (int i = 0; i < 3; i++) {
                        mReplies.add(new Comment("酷酷的忧郁男孩", "http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg",
                                "今天11:00", "行吧", null, 1, null));
                    }
                    commentAdapter.notifyItemRangeInserted(mLastPosition, 3);
                    mLastPosition += 3;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvReply.setLoadMoreComplete();
                            commentAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }



            }
        });
        for (int i = 0; i < 3; i++) {
            mReplies.add(new Comment("帅得惹人骂", "http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg",
                    "今天11:08", "Fine", null, 10, null));
        }
        mLastPosition += 3;
        commentAdapter = new CommentAdapter(this, mReplies);
        rvReply.setAdapter(commentAdapter);
        rvReply.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        title.setText("6条回复");
        Glide.with(this).load(R.mipmap.ic_launcher_round).into(portrait);
        username.setText("Carson");
        likeNum.setText("30");
        time.setText("今天11:30");
        comment.setText("干得漂亮 兄弟！");
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
        text.setTextSize(16);
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
                Toast.makeText(menuMore.getContext(), "举报成功", Toast.LENGTH_SHORT).show();
                window.dismiss();
            }
        });
    }

    private void doLikeWork() {

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
        if (editComment.getHint().toString().startsWith("回复")) {
            //TODO 回复别人
        } else {
            editComment.setHint("回复评论");
//            mReplies.add(new Comment(username.getText().toString(),
//                    ));

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
