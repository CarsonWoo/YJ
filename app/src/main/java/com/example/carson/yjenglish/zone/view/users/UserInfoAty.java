package com.example.carson.yjenglish.zone.view.users;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.ZoneService;
import com.example.carson.yjenglish.zone.model.OtherUsersPlanInfo;
import com.example.carson.yjenglish.zone.model.OthersPlan;
import com.example.carson.yjenglish.zone.model.UserActiveInfo;
import com.example.carson.yjenglish.zone.model.forviewbinder.OtherUsersPlan;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

    private List<OtherUsersPlanInfo.OtherUserInfo.OtherPlanInfo> plans;

    private String author_username;
    private String portraitUrl;
    private String author_signature;
    private String mGender;
    private String learned_word;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_user_info);
        user_id = getIntent().getStringExtra("user_id");
        bindViews();
        executeLoadTask();
//        activeFragment = ActiveFragment.newInstance();
//        planFragment = PlanFragment.newInstance(plans.toString());
    }

    private void executeLoadTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(ZoneService.class).getPlans(UserConfig.getToken(this),
                user_id).enqueue(new Callback<OtherUsersPlanInfo>() {
            @Override
            public void onResponse(Call<OtherUsersPlanInfo> call, Response<OtherUsersPlanInfo> response) {
                OtherUsersPlanInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    author_username = info.getData().getAuthor_username();
                    author_signature = info.getData().getAuthor_personality_signature();
                    portraitUrl = info.getData().getAuthor_portrait();
                    mGender = info.getData().getAuthor_gender();
                    learned_word = info.getData().getLearned_word();

                    plans = info.getData().getIts_plan();

                    List<OthersPlan> othersPlans = new ArrayList<>();
                    for (int i = 0; i < plans.size(); i++) {
                        OthersPlan plan = new OthersPlan();
                        plan.setPlan(plans.get(i).getPlan());
                        plan.setLearned_word_number(plans.get(i).getLearned_word_number());
                        plan.setWord_number(plans.get(i).getWord_number());
                        othersPlans.add(plan);
                    }

                    if (mGender.equals("1")) {
                        tabLayout.getTabAt(0).setText("她的动态");
                        tabLayout.getTabAt(1).setText("她的计划");
                    } else {
                        tabLayout.getTabAt(0).setText("他的动态");
                        tabLayout.getTabAt(1).setText("他的计划");
                    }

                    activeFragment = ActiveFragment.newInstance(user_id);
                    planFragment = PlanFragment.newInstance(othersPlans);
                    initViews();
                } else {
                    Log.e("UserInfo", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<OtherUsersPlanInfo> call, Throwable t) {
                Toast.makeText(UserInfoAty.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindViews() {
        back = findViewById(R.id.back);
        portrait = findViewById(R.id.portrait);
        gender = findViewById(R.id.gender);
        username = findViewById(R.id.username);
        signature = findViewById(R.id.signature);
        signDay = findViewById(R.id.sign_day);
        wordCount = findViewById(R.id.word_count);
        tabLayout = findViewById(R.id.tab_layout);
//        itemDynamic = findViewById(R.id.tab_item_active);
//        itemPlan = findViewById(R.id.tab_item_plan);
        mViewPager = findViewById(R.id.view_pager);
        bg = findViewById(R.id.img_bg);

        tabLayout.addTab(tabLayout.newTab().setText("他的动态"));
        tabLayout.addTab(tabLayout.newTab().setText("他的计划"));

//        if ()

        Glide.with(this).load(R.mipmap.zone_bg_img).thumbnail(0.5f).into(bg);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initViews() {
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        if (mGender.equals("1")) {
            gender.setImageResource(R.mipmap.ic_female);
        } else {
            gender.setImageResource(R.drawable.ic_male);
        }

        Glide.with(this).load(portraitUrl).thumbnail(0.6f).crossFade().into(portrait);
        username.setText(author_username);
        signature.setText(author_signature);
        wordCount.setText(learned_word);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onChange(String insist_day) {
        signDay.setText(insist_day);
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
