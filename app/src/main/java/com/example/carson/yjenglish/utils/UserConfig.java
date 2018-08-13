package com.example.carson.yjenglish.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
    }

    public static boolean isIsFirstTimeUser() {
        return isFirstTimeUser;
    }

    public static void setIsFirstTimeUser(boolean isFirstTimeUser) {
        UserConfig.isFirstTimeUser = isFirstTimeUser;
    }
}
