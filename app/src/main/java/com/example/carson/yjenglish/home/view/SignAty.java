package com.example.carson.yjenglish.home.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
/** 打卡分享页面 */
public class SignAty extends AppCompatActivity {

    private ImageView bg;
    private ImageView back;
    private ImageButton wechatFriend;
    private ImageButton wechat;
    private ImageButton qq;
    private ImageButton qqZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        wechatFriend = findViewById(R.id.btn_wechat_friend);
        wechat = findViewById(R.id.btn_wechat);
        qq = findViewById(R.id.btn_qq);
        qqZone = findViewById(R.id.btn_qq_zone);
        bg = findViewById(R.id.img_bg);


        Glide.with(this).load(R.mipmap.share_img).thumbnail(0.5f).into(bg);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
