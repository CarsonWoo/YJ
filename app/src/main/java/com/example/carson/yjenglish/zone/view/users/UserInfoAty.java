package com.example.carson.yjenglish.zone.view.users;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 点击其他用户头像时跳转至此页面
 */
public class UserInfoAty extends AppCompatActivity implements ActiveFragment.OnActiveItemListener {

    private ImageView back;
    private ImageView bg;
    private CircleImageView portrait;
    private CircleImageView gender;
    private TextView username;
    private TextView signature;
    private TextView signDay;
    private TextView wordCount;
    private TabLayout tabLayout;
    private TabItem itemDynamic;
    private TabItem itemPlan;
    private ViewPager mViewPager;
    private ActiveFragment activeFragment;
    private PlanFragment planFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        activeFragment = ActiveFragment.newInstance();
        planFragment = PlanFragment.newInstance();
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        portrait = findViewById(R.id.portrait);
        gender = findViewById(R.id.gender);
        username = findViewById(R.id.username);
        signature = findViewById(R.id.signature);
        signDay = findViewById(R.id.sign_day);
        wordCount = findViewById(R.id.word_count);
        tabLayout = findViewById(R.id.tab_layout);
        itemDynamic = findViewById(R.id.tab_item_active);
        itemPlan = findViewById(R.id.tab_item_plan);
        mViewPager = findViewById(R.id.view_pager);
        bg = findViewById(R.id.img_bg);

        Glide.with(this).load(R.mipmap.zone_bg_img).thumbnail(0.5f).into(bg);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onItemClick() {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return activeFragment;
            } else {
                return planFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
    }
}
