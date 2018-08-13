package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 84594 on 2018/8/11.
 */

public class CanotSlidingViewPager extends ViewPager {

    private float beforeX;

    private boolean scrollable = true;

    public CanotSlidingViewPager(@NonNull Context context) {
        super(context);
    }

    public CanotSlidingViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //禁止左滑 左滑：上一次坐标 > 当前坐标

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (scrollable) {
            return super.dispatchTouchEvent(ev);
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    beforeX = ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float motionValue = ev.getX() - beforeX;
                    if (motionValue < 0) {
                        return true;
                    }
                    beforeX = ev.getX();
                    break;
                default:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}
