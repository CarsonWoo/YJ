package com.example.carson.yjenglish.home.view.feeds;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.HomeListAdapter;
import com.example.carson.yjenglish.home.AuthorInfoContract;
import com.example.carson.yjenglish.home.AuthorInfoTask;
import com.example.carson.yjenglish.home.model.AuthorInfo;
import com.example.carson.yjenglish.home.model.AuthorModel;
import com.example.carson.yjenglish.home.model.HomeInfo;
import com.example.carson.yjenglish.home.presenter.AuthorInfoPresenter;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorAty extends AppCompatActivity implements HomeListAdapter.OnVideoListener,
    HomeListAdapter.OnItemClickListener, AuthorInfoContract.View {

    private ImageView back;
    private CircleImageView portrait;
    private CircleImageView gender;
    private TextView username;
    private TextView signature;
    private RecyclerView recyclerView;

    private String portraitUrl;
    private String usernameStr;
    private String mGender;
    private String mSignature;
    private String author_id;

    private CollapsingToolbarLayout toolbar;

    private HomeListAdapter mAdapter;

    private List<HomeInfo.Data.Feed> mList;

    private AuthorInfoContract.Presenter presenter;
    private AuthorInfoPresenter authorInfoPresenter;

    private Dialog mDialog;

    private int page = 1;

    private int mScrollDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_author);
        portraitUrl = getIntent().getStringExtra("portrait_url");
        usernameStr = getIntent().getStringExtra("username");
        author_id = getIntent().getStringExtra("author_id");
        mDialog = DialogUtils.getInstance(this).newCommonDialog("加载中", R.mipmap.gif_loading_video, true);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        mDialog.getWindow().setAttributes(lp);
        bindViews();
    }

    private void bindViews() {
        back = findViewById(R.id.back);
        portrait = findViewById(R.id.portrait);
        username = findViewById(R.id.username);
        signature = findViewById(R.id.signature);
        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.collapse_tool_bar);
        gender = findViewById(R.id.gender);

        toolbar.setTitle(usernameStr);
        toolbar.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
        toolbar.setCollapsedTitleTextColor(Color.BLACK);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Glide.with(this).load(portraitUrl).thumbnail(0.5f).into(portrait);
        username.setText(usernameStr);
        executeTask();
//        initRecycler();
    }

    private void executeTask() {
        AuthorInfoTask task = AuthorInfoTask.getInstance();
        authorInfoPresenter = new AuthorInfoPresenter(task, this);
        this.setPresenter(authorInfoPresenter);
        presenter.getAuthorInfo(new AuthorModel(author_id, String.valueOf(page), "10"));
    }

    private void initRecycler() {
        mAdapter = new HomeListAdapter(this, mList, -1, this);
        mAdapter.setItemListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollDistance += dy;
                if (mScrollDistance >= recyclerView.getHeight()) {
                    Log.e("Author", "到底了");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onVideoClick(View view, String url) {

    }

    @Override
    public void onClick(ArrayList item, boolean requestComment, int position) {
        HomeInfo.Data.Feed mItem = (HomeInfo.Data.Feed) item.get(0);
        Intent toDetail = new Intent(this, HomeItemAty.class);
        toDetail.putExtra("id", mItem.getId());
        toDetail.putExtra("video_url", mItem.getVideo());
        toDetail.putExtra("img_url", mItem.getPic());
        toDetail.putExtra("username", mItem.getAuthor_username());
        toDetail.putExtra("title", mItem.getTitle());
        toDetail.putExtra("portrait_url", mItem.getAuthor_portrait());
        toDetail.putExtra("like_num", mItem.getLikes());
        toDetail.putExtra("request_comment", requestComment);
//        toDetail.putExtra("is_like", mItem.getIs_favour().equals("1"));
        startActivity(toDetail);
    }

    @Override
    public void onLikeClick(String id, String is_favour, TextView tv) {

    }

    @Override
    public void setPresenter(AuthorInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(AuthorInfo info) {
        if (info.getStatus().equals("200")) {
            mList = info.getData().getFeeds();
            mSignature = info.getData().getAuthor_personality_signature();
            mGender = info.getData().getAuthor_gender();
            if (mGender.equals("0")) {
                gender.setImageResource(R.drawable.ic_male);
            } else {
                gender.setImageResource(R.mipmap.ic_female);
            }
            signature.setText(mSignature);
            initRecycler();
        } else {
            Log.e("Author", info.getMsg());
        }
    }
}
