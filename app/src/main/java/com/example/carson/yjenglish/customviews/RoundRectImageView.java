package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.carson.yjenglish.R;


/**
 * Created by 84594 on 2018/8/3.
 */

public class RoundRectImageView extends android.support.v7.widget.AppCompatImageView {

    private Path mPath;
    private float mRadius;

    private int mWidth, mHeight;

    public RoundRectImageView(Context context) {
        super(context);
        init();
    }

    public RoundRectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundRectImageView);
        mRadius = a.getFloat(R.styleable.RoundRectImageView_radius, 0f);
        a.recycle();
        init();
    }

    public RoundRectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        super.setScaleType(ScaleType.CENTER_CROP);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        update();
    }

    private void update() {
        mPath = new Path();
        mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight),
                new float[] {mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius},
                Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPath != null) {
            canvas.clipPath(mPath);
        }
        super.onDraw(canvas);
    }
}
