package com.example.carson.yjenglish;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.utils.UserConfig;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.litepal.LitePal;

/**
 * Created by 84594 on 2018/7/30.
 */

public class MyApplication extends Application {

    private static Context context;
    public static IWXAPI mWXApi;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        regist2WX();
    }

    private void regist2WX() {
        mWXApi = WXAPIFactory.createWXAPI(this, UserConfig.WECHAT_APP_ID, false);
        //将app注册到微信
        mWXApi.registerApp(UserConfig.WECHAT_APP_ID);
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
