package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.carson.yjenglish.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 84594 on 2018/8/16.
 */

public class PickerView extends View {
    /**
     * text之间间距和minTextSize之比,手工进行配置
     */
    public static final float MARGIN_ALPHA = 0.2f;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 2;

    private List<String> mDataList;
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private Paint mPaint;
    private Paint mBgPaint;

    private float mMaxTextSize;
    private float mMinTextSize;

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;

    // 要绘制的数据位于当前数据的上方或下方
    private int mUp = -1;
    private int mDown = 1;
    /** 相邻数据间的距离 */
    private float mDistance;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    /** 滑动的距离 */
    private float mMoveLen = 0;
    private Handler mHandler = new Handler();
    private onSelectListener mSelectListener;

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public interface onSelectListener {
        void onSelect(String text, int pos);
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    /** 添加数据 */
    public void setData(List<String> datas) {
        mDataList = datas;
        mCurrentSelected = datas.size() / 2;
        invalidate();
    }

    public void setSelected(int selected) {
        mCurrentSelected = selected;
        int distance = mDataList.size() / 2 - mCurrentSelected;
        if (distance < 0)
            for (int i = 0; i < -distance; i++) {
                moveHeadToTail();
                mCurrentSelected--;
            }
        else if (distance > 0)
            for (int i = 0; i < distance; i++) {
                moveTailToHead();
                mCurrentSelected++;
            }
        invalidate();
    }

    /**
     * 选择选中的内容
     *
     * @param mSelectItem
     */
    public void setSelected(String mSelectItem) {
        for (int i = 0; i < mDataList.size(); i++)
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i);
                break;
            }
    }

    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    private void init() {
        mDataList = new ArrayList<>();
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(Color.parseColor("#305ee1c9"));
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // 按照View的高度计算字体大小
        mMaxTextSize = mViewHeight / 12.0f;
        mMinTextSize = mMaxTextSize;
        mDistance = MARGIN_ALPHA * mViewHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制当前元素
        drawText(canvas, 0, mDown);
        // 绘制上方元素
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawText(canvas, i, mUp);
        }
        // 绘制下方元素
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawText(canvas, i, mDown);
        }
        canvas.drawRect(new RectF(0, (mViewHeight / 2) - mMaxTextSize, mViewWidth,
                (mViewHeight / 2) + mMaxTextSize), mBgPaint);
    }

    /**
     * @param position：当前元素前后第几个位置，若为当前元素position为0
     * @param direction：绘制的元素相对于当前元素的方向,上方为-1，下方为1，当前元素默认为下方
     */
    private void drawText(Canvas canvas, int position, int direction) {
        // 元素距离控件中心的相对距离
        float offset = (float) (mDistance * position + direction * mMoveLen);

        // 缩放倍数：位于控件中心时，缩放倍数是1;控件中心的前一个数据和后一个数据的缩放倍数是0
        float f = (float) (1 - Math.pow(offset / mDistance, 2));// 按抛物线缩放
        // 当数据与控件中心的距离，大于数据间的初始距离时，不再进行缩放
        float scale = f < 0 ? 0 : f;

        // 字体尺寸
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;

        // 字体大小与透明度
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));

        float x = (float) (mViewWidth / 2.0);
        float y = getTextBaseLine((float) (mViewHeight / 2.0 + direction
                * offset));

        canvas.drawText(mDataList.get(mCurrentSelected + direction * position),
                x, y, mPaint);
    }

    // 调整字体的位置，使其垂直居中进行绘制
    private float getTextBaseLine(float p) {
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (p - (fmi.bottom / 2.0 + fmi.top / 2.0));
        return baseline;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if(mHandler!=null){
            mHandler.removeCallbacks(mRunnable);
        }

        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event) {

        mMoveLen += (event.getY() - mLastDownY);

        // 当向下滑动时，mMoveLen为正；当向上滑动时，mMoveLen为负
        if (mMoveLen > mDistance / 2) {
            // 往下滑超过离开距离
            String tail = mDataList.get(mDataList.size() - 1);
            mDataList.remove(mDataList.size() - 1);
            mDataList.add(0, tail);

            // 重新设置数据的位置，将其整体上移
            mMoveLen -= mDistance;
        } else if (mMoveLen < -mDistance / 2) {
            // 往上滑超过离开距离
            String head = mDataList.get(0);
            mDataList.remove(0);
            mDataList.add(head);

            // 重新设置数据的位置，将其整体下移
            mMoveLen += mDistance;
        }

        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp(MotionEvent event) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }

        if(mHandler!=null){
            mHandler.removeCallbacks(mRunnable);
        }

        mHandler.postDelayed(mRunnable, 10);
    }

    public String getText() {
        return mDataList.get(mCurrentSelected);
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mHandler != null) {
                    mHandler.removeCallbacks(this);
                    // 事件监听
                    if (mSelectListener != null)
                        mSelectListener.onSelect(mDataList
                                .get(mCurrentSelected), mCurrentSelected);
                }
            } else{
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                // 用于将数据回滚到起始位置或者终点位置
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
                mHandler.postDelayed(this, 10);
            }
            invalidate();
        }
    };
}
