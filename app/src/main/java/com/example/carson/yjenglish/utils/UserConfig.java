package com.example.carson.yjenglish.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by 84594 on 2018/7/30.
 */

public class UserConfig {

    private static String APP_ID = "YJEnglish";

    public static final int ALARM_START_STUDY_ID = 996;

//    public static final String HOST = "http://123.207.85.37:8080/";

    public static final String HOST = "http://47.107.62.22:8080/";

    public static final String TMP_HOST = "http://beibei.yiluzou.cn:8080/";

    public static final String WECHAT_APP_ID = "wx3a33fd1f25154473";

    public static final String QQ_APP_ID = "1107820868";

    public static void cacheToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString("token", "");
    }

    public static void cacheUsername(Context context, String username) {
        SharedPreferences sp = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public static String getUsername(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
                .getString("username", "独角鲸");
    }

    public static void clearUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static void cacheIsFirstTimeUser(Context ctx, boolean isFirstTimeUser) {
        SharedPreferences sp = ctx.getSharedPreferences("isFirstTimeUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("is_first_time_user", isFirstTimeUser);
        editor.apply();
    }

    public static boolean getIsFirstTimeUser(Context ctx) {
        return ctx.getSharedPreferences("isFirstTimeUser", Context.MODE_PRIVATE).getBoolean("is_first_time_user", true);
    }

    public static void cacheLastDate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Date date = new Date();
        String pattern = "yyyy年MM月dd日";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(pattern);
        String dateStr = df.format(date);
        editor.putString("lastDate", dateStr);
        editor.apply();
    }

    public static String getLastDate(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString("lastDate", null);
    }

    public static void cachePhone(Context context, String phone) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putString("phone", phone).apply();
    }

    public static String getPhone(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString("phone", null);
    }

    public static void cacheQQ(Context context, String QQNum) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putString("QQ", QQNum).apply();
    }

    public static String getQQ(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString("QQ", null);
    }

    public static void cacheWechat(Context ctx, String wechatName) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putString("wechat", wechatName).apply();
    }

    public static String getWechat(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString("wechat", null);
    }

    public static void cacheShouldSendNotification(Context context, boolean shouldSend) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putBoolean("should_send_notification", shouldSend).apply();
    }

    public static boolean shouldSendNotification(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getBoolean("should_send_notification", true);
    }

    public static void cacheNotificationTime(Context context, String hour, String minute) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putString("notification_time", hour + ";" + minute).apply();
    }

    public static String getNotificationTime(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString("notification_time", "08时;00分");
    }

    public static void cacheHasPlan(Context context, boolean hasPlan) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putBoolean("has_plan", hasPlan).apply();
    }

    public static boolean HasPlan(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getBoolean("has_plan", false);
    }

    public static void cacheSelectedPlan(Context context, String plan) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putString("selected_plan", plan).apply();
    }

    public static String getSelectedPlan(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString("selected_plan", "");
    }

    public static void cacheDailyWord(Context context, String daily_count) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putString("daily_word", daily_count).apply();
    }

    public static String getDailyWord(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString("daily_word", "");
    }
}
