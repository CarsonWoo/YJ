package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by 84594 on 2018/8/15.
 */

public class CannotDragSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    public CannotDragSeekBar(Context context) {
        super(context);
    }

    public CannotDragSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CannotDragSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }
}
