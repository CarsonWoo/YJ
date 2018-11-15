package com.example.carson.yjenglish;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.register.view.RegisterActivity;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import java.io.File;

public class StartActivity extends AppCompatActivity {

    private ConstraintLayout mLayout;

    private int times = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mLayout = findViewById(R.id.start_layout);


        requestPermission();


    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //2、申请权限: 参数二：权限的数组；参数三：请求码
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        } else {
            File recordDirectory = new File(Environment.getExternalStorageDirectory().getPath() +
                    "/背呗背单词/");
            if (!recordDirectory.exists()) {
                recordDirectory.mkdirs();
                Log.e("Start", "cacheDir");
            }

            mLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!UserConfig.getToken(StartActivity.this).isEmpty()) {
                        startActivity(new Intent(StartActivity.this, HomeActivity.class));
                        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    } else {
                        if (UserConfig.getIsFirstTimeUser(MyApplication.getContext())) {
                            startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                            overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                        } else {
                            startActivity(new Intent(StartActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                        }
                    }
                    finish();
                }
            }, 2000);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                File recordDirectory = new File(Environment.getExternalStorageDirectory().getPath() +
                        "/背呗背单词/");
                if (!recordDirectory.exists()) {
                    recordDirectory.mkdirs();
                    Log.e("Start", "cacheDir");
                }

                mLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!UserConfig.getToken(StartActivity.this).isEmpty()) {
                            startActivity(new Intent(StartActivity.this, HomeActivity.class));
                            overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                        } else {
                            if (UserConfig.getIsFirstTimeUser(MyApplication.getContext())) {
                                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                            } else {
                                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                            }
                        }
                        finish();
                    }
                }, 2000);
            } else {
                if (times < 1) {
                    DialogUtils du = DialogUtils.getInstance(this);
                    Dialog mDialog = du.newTipsDialog("取消授权将可能导致音频无法播放噢~小呗建议您授权噢~",
                            View.TEXT_ALIGNMENT_CENTER);
                    mDialog.show();
                    WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
                    lp.width = ScreenUtils.dp2px(this, 260);
                    lp.height = ScreenUtils.dp2px(this, 240);
                    lp.gravity = Gravity.CENTER;
                    mDialog.getWindow().setAttributes(lp);

                    du.setTipsListener(new DialogUtils.OnTipsListener() {
                        @Override
                        public void onConfirm() {
                            requestPermission();
                        }

                        @Override
                        public void onCancel() {
                            requestPermission();
                        }
                    });
                    times ++;
                } else {
                    if (!UserConfig.getToken(StartActivity.this).isEmpty()) {
                        startActivity(new Intent(StartActivity.this, HomeActivity.class));
                        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    } else {
                        if (UserConfig.getIsFirstTimeUser(MyApplication.getContext())) {
                            startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                            overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                        } else {
                            startActivity(new Intent(StartActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                        }
                    }
                    finish();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
