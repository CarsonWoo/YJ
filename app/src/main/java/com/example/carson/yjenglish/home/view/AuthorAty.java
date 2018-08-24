package com.example.carson.yjenglish.home.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.HomeListAdapter;
import com.example.carson.yjenglish.home.model.HomeItem;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorAty extends AppCompatActivity implements HomeListAdapter.OnVideoListener,
HomeListAdapter.OnItemClickListener{

    private ImageView back;
    private CircleImageView portrait;
    private TextView username;
    private TextView signature;
    private RecyclerView recyclerView;

    private String portraitUrl;
    private String usernameStr;

    private HomeListAdapter mAdapter;

    private List<HomeItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        portraitUrl = getIntent().getStringExtra("portrait_url");
        usernameStr = getIntent().getStringExtra("username");
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        portrait = findViewById(R.id.portrait);
        username = findViewById(R.id.username);
        signature = findViewById(R.id.signature);
        recyclerView = findViewById(R.id.recycler_view);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Glide.with(this).load(portraitUrl).thumbnail(0.8f).into(portrait);
        username.setText(usernameStr);
        signature.setText("");
        initRecycler();
    }

    private void initRecycler() {
        mList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            HomeItem data = new HomeItem();
            data.setCommentNum(i * 10);
            data.setLikeNum(i * 20);
            data.setUsername("Carson");
            if (i == 0 || i == 1) {
                data.setVideoUrl("http://dn-chunyu.qbox.me/fwb/static/images/home/video/video_aboutCY_A.mp4");
                data.setImgUrl("http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg");
                data.setPortraitUrl("http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg");
            } else {
                data.setImgUrl("http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg");
                data.setPortraitUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg");
            }
            mList.add(data);
        }
        mAdapter = new HomeListAdapter(this, mList, -1, this);
        mAdapter.setItemListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
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
    public void onClick(ArrayList item, boolean requestComment) {

    }
}
