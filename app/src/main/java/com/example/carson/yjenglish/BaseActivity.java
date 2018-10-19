package com.example.carson.yjenglish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;

/**
 * Created by 84594 on 2018/7/28.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        setContentView(getLayoutResource());
    }

    public abstract int getLayoutResource();

    public static void tokenOutOfDate(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_right_sign_out);
    }

    protected void setSwipeBack(Activity activity) {
        SwipeBackHelper.onCreate(activity);
        SwipeBackHelper.getCurrentPage(activity)
                .setClosePercent(0.5f)
                .setSwipeEdgePercent(0.15f)
                .setSwipeRelateOffset(300)
                .setSwipeSensitivity(1.0f)
                .setSwipeBackEnable(true)
                .setSwipeRelateEnable(true);
    }

}
