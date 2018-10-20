package com.example.carson.yjenglish.zone.view.plan;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.zone.GetPlanTask;
import com.example.carson.yjenglish.zone.presenter.GetPlanPresenter;

import java.util.ArrayList;
import java.util.List;

public class PlanAddAty extends AppCompatActivity {

    private ImageView back;
    private Button hit, primary, junior, senior, college, foreign, elses;

    private List<Button> mTags = new ArrayList<>();

    private HitTab mHitTab;
    private PrimaryTab mPriTab;
    private JuniorTab mJuniorTab;
    private SeniorTab mSeniorTab;
    private CollegeTab mCollegeTab;
    private ForeignTab mForTab;
    private ElseTab mElseTab;

    public static final int INTENT_FROM_HOME = 0;
    public static final int INTENT_FROM_PLAN = 1;

    protected static int fromIntent;

    private int mCurPos = 0;

    private GetPlanPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_plan_add);

        fromIntent = getIntent().getIntExtra("from_intent", INTENT_FROM_PLAN);

        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        hit = findViewById(R.id.tag_hit);
        primary = findViewById(R.id.tag_primary);
        junior = findViewById(R.id.tag_junior);
        senior = findViewById(R.id.tag_senior);
        college = findViewById(R.id.tag_college);
        foreign = findViewById(R.id.tag_foreign);
        elses = findViewById(R.id.tag_else);

        mTags.add(hit);
        mTags.add(primary);
        mTags.add(junior);
        mTags.add(senior);
        mTags.add(college);
        mTags.add(foreign);
        mTags.add(elses);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initContainer();
    }

    //控制标签的选择 并切换页面
    private void initContainer() {
        setDefaultTab();
        for (int i = 0; i < 7; i++) {
            final int j = i;
            mTags.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetSelected();
                    if (mCurPos != j) {
                        mTags.get(j).setSelected(true);
                        mTags.get(j).setTextColor(Color.WHITE);
                        mCurPos = j;
                        switchTab(j);
                    }
                }
            });
        }
    }

    private void setDefaultTab() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        mTags.get(0).setSelected(true);
        mTags.get(0).setTextColor(Color.WHITE);
        mHitTab = HitTab.newInstance();
        GetPlanTask task = GetPlanTask.getInstance();
        presenter = new GetPlanPresenter(task, mHitTab);
        mHitTab.setPresenter(presenter);
        transaction.add(R.id.plan_container, mHitTab);
        transaction.commit();
    }

    private void switchTab(int tabInt) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        hideAllFragments(transaction);
        switch (tabInt) {
            case 0:
                transaction.show(mHitTab);
                GetPlanTask task = GetPlanTask.getInstance();
                presenter = new GetPlanPresenter(task, mHitTab);
                mHitTab.setPresenter(presenter);
                break;
            case 1:
                if (mPriTab == null) {
                    mPriTab = PrimaryTab.newInstance();
                    transaction.add(R.id.plan_container, mPriTab);
                } else {
                    transaction.show(mPriTab);
                }
                GetPlanTask priTask = GetPlanTask.getInstance();
                presenter = new GetPlanPresenter(priTask, mPriTab);
                mPriTab.setPresenter(presenter);
                break;
            case 2:
                if (mJuniorTab == null) {
                    mJuniorTab = JuniorTab.newInstance();
                    transaction.add(R.id.plan_container, mJuniorTab);
                } else {
                    transaction.show(mJuniorTab);
                }
                GetPlanTask juniorTask = GetPlanTask.getInstance();
                presenter = new GetPlanPresenter(juniorTask, mJuniorTab);
                mJuniorTab.setPresenter(presenter);
                break;
            case 3:
                if (mSeniorTab == null) {
                    mSeniorTab = SeniorTab.newInstance();
                    transaction.add(R.id.plan_container, mSeniorTab);
                } else {
                    transaction.show(mSeniorTab);
                }
                GetPlanTask seniorTask = GetPlanTask.getInstance();
                presenter = new GetPlanPresenter(seniorTask, mSeniorTab);
                mSeniorTab.setPresenter(presenter);
                break;
            case 4:
                if (mCollegeTab == null) {
                    mCollegeTab = CollegeTab.newInstance();
                    transaction.add(R.id.plan_container, mCollegeTab);
                } else {
                    transaction.show(mCollegeTab);
                }
                GetPlanTask collegeTask = GetPlanTask.getInstance();
                presenter = new GetPlanPresenter(collegeTask, mCollegeTab);
                mCollegeTab.setPresenter(presenter);
                break;
            case 5:
                if (mForTab == null) {
                    mForTab = ForeignTab.newInstance();
                    transaction.add(R.id.plan_container, mForTab);
                } else {
                    transaction.show(mForTab);
                }
                GetPlanTask forTask = GetPlanTask.getInstance();
                presenter = new GetPlanPresenter(forTask, mForTab);
                mForTab.setPresenter(presenter);
                break;
            case 6:
                if (mElseTab == null) {
                    mElseTab = ElseTab.newInstance();
                    transaction.add(R.id.plan_container, mElseTab);
                } else {
                    transaction.show(mElseTab);
                }
                GetPlanTask elseTask = GetPlanTask.getInstance();
                presenter = new GetPlanPresenter(elseTask, mElseTab);
                mElseTab.setPresenter(presenter);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideAllFragments(FragmentTransaction ft) {
        if (mHitTab != null) {
            ft.hide(mHitTab);
        }
        if (mPriTab != null) {
            ft.hide(mPriTab);
        }
        if (mJuniorTab != null) {
            ft.hide(mJuniorTab);
        }
        if (mSeniorTab != null) {
            ft.hide(mSeniorTab);
        }
        if (mCollegeTab != null) {
            ft.hide(mCollegeTab);
        }
        if (mForTab != null) {
            ft.hide(mForTab);
        }
        if (mElseTab != null) {
            ft.hide(mElseTab);
        }
//        ft.commit();
    }

    private void resetSelected() {
        for (Button btn : mTags) {
            btn.setSelected(false);
            btn.setTextColor(Color.parseColor("#5ee1c9"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
