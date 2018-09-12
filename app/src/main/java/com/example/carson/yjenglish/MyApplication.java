package com.example.carson.yjenglish;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

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
//        Log.e("Application", context.getClassLoader().toString());
        return context;

    }

    public static int getVersionCode(Context ctx) {
        int code = 0;
        PackageManager pm = ctx.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            code = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }
}
