package com.example.carson.yjenglish;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jude.swipbackhelper.SwipeBackHelper;

/**
 * Created by 84594 on 2018/7/28.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
    }

    public abstract int getLayoutResource();

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
