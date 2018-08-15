package com.example.carson.yjenglish.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 84594 on 2018/7/30.
 */

public class UserConfig {

    private static String APP_ID = "YJEnglish";
    private static boolean isFirstTimeUser = true;

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

    public static void clearToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean isIsFirstTimeUser() {
        return isFirstTimeUser;
    }

    public static void setIsFirstTimeUser(boolean isFirstTimeUser) {
        UserConfig.isFirstTimeUser = isFirstTimeUser;
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
}
