package com.example.carson.yjenglish;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by 84594 on 2018/7/30.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext() {
        return context;
    }
}
