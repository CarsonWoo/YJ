package com.example.carson.yjenglish.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.utils.FontUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 84594 on 2018/8/14.
 */

public class MyCalendarView extends View {

    private String TAG = "MyCalendarView";

    private Paint mPaint;
    private Paint bgPaint;

    /** 月份的颜色、大小 */
    private int mTextMonthColor;
    private float mTextSizeMonth;
    /** 星期的颜色、大小*/
    private int mTextColorWeek;
    private float mTextSizeWeek;
    /** 日期文本的颜色、大小*/
    private int mTextColorDay;
    private float mTextSizeDay;
    /** 选中的文本的颜色*/
    private int mSelectTextColor;

    private float weekMarginTop = 220;

    private String pattern = "yyyy年MM月";

    private Date month;//当前月份
    private boolean isCurMonth;//展示的月份是否为当前月份
    private int currentDay, selectDay, selectMonth;//当前日期, 选中的日期, 选中的月份

    private int dayOfMonth;    //月份天数
    private int firstIndex;    //当月第一天位置索引
    private int todayWeekIndex;//今天是星期几
    private int firstLineNum, lastLineNum; //第一行、最后一行能展示多少日期
    private int lineNum;      //日期行数
    private String[] WEEK_STR = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    private String[] MONTH_STR_TITLE = new String[]{"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};

    private List<Integer> mSelectDays;

    /** 行间距*/
    private float mLineSpac = 30;
    /** 字体上下间距*/
    private float mTextSpac = 30;


    private float titleHeight, weekHeight, dayHeight, rowHeight;
    private float columnWidth;

    public MyCalendarView(Context context) {
        this(context, null);
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCalendarView, defStyleAttr, 0);
        mTextSizeMonth = a.getDimension(R.styleable.MyCalendarView_mTextSizeMonth, 100);
        mTextMonthColor = a.getColor(R.styleable.MyCalendarView_mTextColorMonth, getResources().getColor(R.color.colorText));
        mTextSizeWeek = a.getDimension(R.styleable.MyCalendarView_mTextSizeWeek, 30);
        mTextColorWeek = a.getColor(R.styleable.MyCalendarView_mTextColorWeek, getResources().getColor(R.color.colorTextWord));
        mTextSizeDay = a.getDimension(R.styleable.MyCalendarView_mTextSizeDay, 40);
        mTextColorDay = a.getColor(R.styleable.MyCalendarView_mTextColorDay, getResources().getColor(R.color.colorTextWord));
        mSelectTextColor = a.getColor(R.styleable.MyCalendarView_mTextColorSelected, Color.WHITE);
        a.recycle();
        initCompute();
    }

    private void initCompute() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setTextSize(mTextSizeMonth);
        titleHeight = FontUtils.getFontHeight(mPaint) + 20;

        mPaint.setTextSize(mTextSizeWeek);
        weekHeight = FontUtils.getFontHeight(mPaint);

        mPaint.setTextSize(mTextSizeDay);
        dayHeight = FontUtils.getFontHeight(mPaint);

        rowHeight = mLineSpac + mTextSpac + dayHeight;

        String cDateStr = getMonthStr(new Date());
        setMonth(cDateStr);

    }

    private String getMonthStr(Date month) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(month);
    }

    private Date str2Date(String str) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setMonth(String date) {
        month = str2Date(date);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        //获取当前日期
        currentDay = c.get(Calendar.DAY_OF_MONTH);
        todayWeekIndex = c.get(Calendar.DAY_OF_WEEK) - 1;

        Date cM = str2Date(getMonthStr(new Date()));

        //判断是否为当月
        if (cM != null) {
            if(cM.getTime() == month.getTime()){
                isCurMonth = true;
            }else{
                isCurMonth = false;
            }
        }
//        Log.e(TAG, "设置月份："+month+"   今天"+currentDay+"号, 是否为当前月："+isCurMonth);
        c.setTime(month);

        dayOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        //第一行1号显示在什么位置（星期几） 从星期天开始算则需 - 1 从星期一开始算则需 - 2
        firstIndex = c.get(Calendar.DAY_OF_WEEK) - 1;
        lineNum = 1;
        //第一行能展示的天数
        firstLineNum = 7 - firstIndex;
        lastLineNum = 0;
        int shengyu = dayOfMonth - firstLineNum;
        while (shengyu > 7){
            lineNum ++;
            shengyu -= 7;
        }
        if(shengyu > 0){
            lineNum ++;
            lastLineNum = shengyu;
        }
//        Log.e(TAG, getMonthStr(month) + "一共有" + dayOfMonth + "天,第一天的索引是：" + firstIndex +
//                "   有" + lineNum + "行，第一行"+firstLineNum + "个，最后一行" + lastLineNum + "个");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽度为布满父布局
        int widthSize = MeasureSpec.getSize(widthMeasureSpec); //获取宽的尺寸
        columnWidth = widthSize / 7;
        //高度 = 标题高度 + 星期距离标题高度 + 星期高度 + 日期行数*每行高度
        float height = titleHeight + weekMarginTop + weekHeight + lineNum * rowHeight;
//        Log.e(TAG, "标题高度："+titleHeight+" 星期高度："+weekHeight+" 每行高度："+ rowHeight +
//                " 行数："+ lineNum + "  \n控件高度："+height);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                (int) height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMonth(canvas);
        drawWeek(canvas);
        drawDate(canvas);
    }

    /**
     * 绘制月份
     * @param canvas
     */
    private void drawMonth(Canvas canvas) {
        //绘制月份
        mPaint.setTextSize(mTextSizeMonth);
        mPaint.setColor(mTextMonthColor);
        String str = getMonthStr(month);
        String year = str.substring(0, 4);
        String monthStr = str.substring(5, 7);
        if (monthStr.startsWith("0")) {
            monthStr = monthStr.substring(1, 2);
        }
//        Log.e(TAG, "year = " + year + " month = " + monthStr);
        int monthIndex = Integer.parseInt(monthStr);
        String title = MONTH_STR_TITLE[monthIndex - 1] + " " + year;
        float textLen = FontUtils.getFontlength(mPaint, title);
        float textStart = (getWidth() - textLen) / 2;
        canvas.drawText(title, textStart, 20 + FontUtils.getFontLeading(mPaint), mPaint);
    }

    /**
     * 绘制星期
     * @param canvas
     */
    private void drawWeek(Canvas canvas) {
        //绘制星期：7天
        mPaint.setTextSize(mTextSizeWeek);
        mPaint.setColor(mTextColorWeek);
        for (int i = 0; i < 7; i++) {
            int len = (int) FontUtils.getFontlength(mPaint, WEEK_STR[i]);
            int x = (int) (i * columnWidth + (columnWidth - len) / 2);
            canvas.drawText(WEEK_STR[i], x, titleHeight + weekMarginTop + FontUtils.getFontLeading(mPaint), mPaint);
        }
    }

    /**
     * 绘制日期
     * @param canvas
     */
    private void drawDate(Canvas canvas) {
        //某行开始绘制的Y坐标，第一行开始的坐标为标题高度+星期部分高度
        float top = titleHeight + weekMarginTop + weekHeight;
        for (int line = 0; line < lineNum; line++) {
            if (line == 0) {
                //第一行
                drawDay(canvas, top, firstLineNum, 0, firstIndex);
            } else if (line == lineNum - 1) {
                top += rowHeight;
                drawDay(canvas, top, lastLineNum, firstLineNum + (line - 1) * 7, 0);
            } else {
                //绘制7天
                top += rowHeight;
                drawDay(canvas, top, 7, firstLineNum + (line - 1) * 7, 0);
            }
        }
    }

    private void drawDay(Canvas canvas, float top, int count, int overday, int startIndex) {
        mPaint.setTextSize(mTextSizeDay);
        float dayTextLeading = FontUtils.getFontLeading(mPaint);

        for (int i = 0; i < count; i++) {
            int left = (int) ((startIndex + i) * columnWidth);
            int day = (overday + i + 1);
            mPaint.setColor(mTextColorDay);
            //每当点进来的时候 当天一定完成单词背诵 直接对当天日期进行打卡
            if (isCurMonth && currentDay == day) {
                mPaint.setColor(mSelectTextColor);
                bgPaint.setColor(getResources().getColor(R.color.colorAccent));
                bgPaint.setStyle(Paint.Style.FILL);
                bgPaint.setStrokeWidth(3);
                canvas.drawCircle(left + columnWidth / 2, top + mLineSpac + dayHeight / 2,
                        40, bgPaint);
            }
            //画完就还原
            bgPaint.setStrokeWidth(0);

            //如果day在selectdays里面
            if (mSelectDays != null) {
                for (int mSelectDay : mSelectDays) {
                    if (mSelectDay == day) {
                        mPaint.setColor(mSelectTextColor);
                        bgPaint.setColor(getResources().getColor(R.color.colorAccent));
                        bgPaint.setStyle(Paint.Style.FILL);
                        bgPaint.setStrokeWidth(3);
                        canvas.drawCircle(left + columnWidth / 2, top + mLineSpac + dayHeight / 2,
                                40, bgPaint);
                    }
                }
            }

            int len = (int) FontUtils.getFontlength(mPaint, day + "");
            int x = (int) (left + (columnWidth - len) / 2);
            canvas.drawText(day + "", x, top + dayTextLeading + mLineSpac, mPaint);
        }
    }

    @Override
    public void invalidate() {
        requestLayout();
        super.invalidate();
    }

    /**月份增减*/
    public void monthChange(int change){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);
        calendar.add(Calendar.MONTH, change);
        setMonth(getMonthStr(calendar.getTime()));
        invalidate();
    }

    public void setmSelectDays(List<Integer> mList) {
        mSelectDays = mList;
    }
}
