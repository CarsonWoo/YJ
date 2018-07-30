package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.carson.yjenglish.R;

/**
 * Created by 84594 on 2018/7/30.
 */

public class BaselineTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mLinePaint;

    private int mLineColor = Color.BLACK;

    public BaselineTextView(Context context) {
        super(context);
        initViews();
    }

    public BaselineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaselineTextView);
        mLineColor = a.getColor(R.styleable.BaselineTextView_baseline_color, mLineColor);
        a.recycle();
        initViews();
    }

    public BaselineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(2f);
        mLinePaint.setColor(mLineColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        float offset = (getHeight() - getTextSize()) / 2;
        float height = getHeight() - offset;

        canvas.drawLine(0, height, width, height, mLinePaint);
    }
}
