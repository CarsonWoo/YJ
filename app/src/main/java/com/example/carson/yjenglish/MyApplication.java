package com.example.carson.yjenglish;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.utils.UserConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
        //发布时第三个参数要改为false 测试时可为true

//        //减轻上报负担 只上报主线程crash
//        String packageName = context.getPackageName();
//        String processName = getProcessName(android.os.Process.myPid());
//
//        //设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));

//        CrashReport.initCrashReport(getApplicationContext(), "e48367ed64", true, strategy);

        Bugly.init(getApplicationContext(), "e48367ed64", true);

        LitePal.initialize(context);
        regist2WX();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);

        //安装tinker
        Beta.installTinker();
    }

    private static String getProcessName(int pid) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = br.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void regist2WX() {
        mWXApi = WXAPIFactory.createWXAPI(this, UserConfig.WECHAT_APP_ID, true);
        //将app注册到微信
        mWXApi.registerApp(UserConfig.WECHAT_APP_ID);
    }

    public static Context getContext() {
//        Log.e("Application", context.getClassLoader().toString());
        return context;

    }

    public static String getVersionCode(Context ctx) {
        int code = 0;
        String versionName = "1.0";
        PackageManager pm = ctx.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            code = pi.versionCode;
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName + "." + code;
    }
}
