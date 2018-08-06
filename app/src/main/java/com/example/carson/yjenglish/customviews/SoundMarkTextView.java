package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 84594 on 2018/8/3.
 */

public class SoundMarkTextView extends android.support.v7.widget.AppCompatTextView {
    public SoundMarkTextView(Context context) {
        super(context);
        init();
    }

    public SoundMarkTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SoundMarkTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "segoeui.ttf");
        setTypeface(tf);
    }
}
