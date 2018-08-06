package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.carson.yjenglish.R;

import java.awt.font.TextAttribute;

/**
 * Created by 84594 on 2018/8/2.
 */

public class MyProgressView extends View {

    private Paint mProgressPaint;

    private int mPaintColorRes = getResources().getColor(R.color.colorAccent);
    private Paint mTrackPaint;

    private int mTrackColorRes = getResources().getColor(R.color.colorAccent);
    //    private int mTrackColorRes = Color.BLUE;
    private Paint mBgPaint;
    private int mBgColorRes = getResources().getColor(R.color.test2);

    private float progress = 0f;

    public MyProgressView(Context context) {
        super(context);
        initPaint();
    }

    public MyProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyProgressView);
        if (a.hasValue(R.styleable.MyProgressView_progress)) {
            progress = a.getFloat(R.styleable.MyProgressView_progress, 0f);
        }
        a.recycle();
        initPaint();
    }

    public MyProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mPaintColorRes);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(15);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mTrackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrackPaint.setColor(mTrackColorRes);
        mTrackPaint.setStyle(Paint.Style.STROKE);
        mTrackPaint.setStrokeWidth((float) 4);

        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setColor(mBgColorRes);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mBgPaint.setStrokeJoin(Paint.Join.ROUND);
        mBgPaint.setStrokeWidth((float) 4);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            //最大模式，即wrap_content
            setMeasuredDimension(600, 600);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            //高度为指定高度
            setMeasuredDimension(600, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            //宽度为指定宽度
            setMeasuredDimension(widthSpecSize, 600);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingBottom - paddingTop;
//        canvas.drawLine(170 + paddingLeft, height + paddingTop - 48,
//                190 + paddingLeft, height + paddingTop - 80, mBgPaint);
        canvas.drawArc(paddingLeft, paddingTop, width + paddingLeft,
                height + paddingTop, -240, 300, false, mBgPaint);
        canvas.drawArc(20 + paddingLeft, 20 + paddingTop, width - 20 + paddingLeft,
                height - 20 + paddingTop, -238, 296, false, mTrackPaint);
        canvas.drawArc(40 + paddingLeft, 40 + paddingTop, width - 40 + paddingLeft,
                height - 40 + paddingTop, -240, 300, false, mBgPaint);
//        canvas.drawLine(width - 170 + paddingLeft, height + paddingTop - 48,
//                width - 190 + paddingLeft, height + paddingTop - 80, mBgPaint);
        canvas.drawArc(20 + paddingLeft, 20 + paddingTop, width - 20 + paddingLeft,
                height - 20 + paddingTop, -237, 295 * (progress / 100), false, mProgressPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
