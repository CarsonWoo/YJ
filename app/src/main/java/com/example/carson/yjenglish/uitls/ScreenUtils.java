package com.example.carson.yjenglish.uitls;

import android.content.Context;

/**
 * Created by 84594 on 2018/8/3.
 */

public class ScreenUtils {
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
