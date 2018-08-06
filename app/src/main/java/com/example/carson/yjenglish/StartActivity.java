package com.example.carson.yjenglish;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.utils.UserConfig;

public class StartActivity extends AppCompatActivity {

    private ConstraintLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mLayout = findViewById(R.id.start_layout);

//        ObjectAnimator animator = ObjectAnimator.ofFloat(mLayout, "rotationY", 0, 90).setDuration(500);
//        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mLayout, "rotationY", 270, 360).setDuration(500);
//        final AnimatorSet set = new AnimatorSet();
//        set.play(animator).before(animator1);
//        mLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                set.start();
//            }
//        }, 1000);


        mLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!UserConfig.getToken(StartActivity.this).isEmpty()) {
                    startActivity(new Intent(StartActivity.this, HomeActivity.class));
                    overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                } else {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                }
                finish();
            }
        }, 2000);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
