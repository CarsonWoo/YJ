package com.example.carson.yjenglish.home.view.word;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.CanotSlidingViewPager;
import com.example.carson.yjenglish.customviews.MyCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 打卡成功才显示的页面 */
public class SignInAty extends AppCompatActivity {


    private ImageView bg;
    private ImageView back;
    private CanotSlidingViewPager vp;
    private int mCurMonth = 11;
    private List<Integer> mSelectDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
    }

    @SuppressLint("UseSparseArrays")
    private void initViews() {
        bg = findViewById(R.id.img_bg);
        back = findViewById(R.id.back);
        vp = findViewById(R.id.view_pager);
        Glide.with(this).load(R.drawable.start_img).thumbnail(0.7f).into(bg);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
        String dateStr = df.format(date);

        mSelectDays = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mSelectDays.add(i + 3);
        }

        vp.setAdapter(new MyPagerAdapter());
        vp.setCurrentItem(mCurMonth);
        vp.setOffscreenPageLimit(2);
        vp.setPageMargin(70);
        vp.setPageTransformer(false, new AlphaTransformer());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    private class MyPagerAdapter extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = LayoutInflater.from(SignInAty.this).inflate(R.layout.layout_calendar, null, false);
            MyCalendarView cal = itemView.findViewById(R.id.calendar);
            cal.monthChange(position - mCurMonth);
            cal.setmSelectDays(mSelectDays);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private class AlphaTransformer implements ViewPager.PageTransformer {
        private float DEFAULT_ALPHA = 0.5f;
        /**
         * position取值特点：
         * 假设页面从0～1，则：
         * 第一个页面position变化为[0,-1]
         * 第二个页面position变化为[1,0]
         *
         * @param page
         * @param position
         */
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position < -1 || position > 1) {
                page.setAlpha(DEFAULT_ALPHA);
            } else {
                //不透明 -> 半透明
                if (position < 0) {//[0, -1]
                    page.setAlpha(DEFAULT_ALPHA + (1 + position) * (1 - DEFAULT_ALPHA));
                } else {
                    page.setAlpha(DEFAULT_ALPHA + (1 - position) * (1 - DEFAULT_ALPHA));
                }
            }
        }
    }
}
