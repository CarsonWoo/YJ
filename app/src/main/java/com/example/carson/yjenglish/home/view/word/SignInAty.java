package com.example.carson.yjenglish.home.view.word;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.CanotSlidingViewPager;
import com.example.carson.yjenglish.customviews.MyCalendarView;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.model.CalendarInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/** 打卡成功才显示的页面 */
public class SignInAty extends AppCompatActivity {


    private ImageView bg;
    private ImageView back;
    private CanotSlidingViewPager vp;
    private int mCurMonth = 11;
    private Map<Integer, List<Integer>> mSelectDays;

    private Dialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        getWindow().setStatusBarColor(Color.BLACK);
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_sign_in);
//        mSelectDays = new HashMap<>();
//        for (int i = 0; i >= -11; i--) {
//            List<Integer> list = new ArrayList<>();
//            list.add(-i + 1);
//            mSelectDays.put(i, list);
//        }
        executeTask();
//        initViews();
    }

    private void executeTask() {
        mDialog = DialogUtils.getInstance(this).newCommonDialog("获取打卡信息中",
                R.mipmap.gif_loading_video, true);
        mDialog.show();

        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 240);
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);

        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).getHistory(UserConfig.getToken(this))
                .enqueue(new Callback<CalendarInfo>() {
                    @Override
                    public void onResponse(Call<CalendarInfo> call, Response<CalendarInfo> response) {
                        CalendarInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            Map<String, List<Integer>> map = info.getData();
                            mSelectDays = new HashMap<>();
                            for (String key : map.keySet()) {
                                mSelectDays.put(Integer.parseInt(key), map.get(key));
                            }
                            mDialog.dismiss();
                            initViews();
                        }
                    }

                    @Override
                    public void onFailure(Call<CalendarInfo> call, Throwable t) {

                    }
                });
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
            Log.e("SignInAty", "position = " + position);
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
