package com.example.carson.yjenglish.zone.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.PlanAdapter;
import com.example.carson.yjenglish.customviews.BaselineTextView;
import com.example.carson.yjenglish.zone.model.PlanData;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity {

    /**
     * 若改变了计划 可通过广播通知HomeFragment更改头部局
     */

    private final String TAG = getClass().getSimpleName();

    private ImageView back;
    private TextView edit;
    private FrameLayout container;

    private RecyclerView mPlanRv;
    private PlanAdapter mAdapter;
    private List<PlanData> mPlans;
    private TextView wordTag;
    private TextView remain;
    private TextView expectedDay;

    private boolean hasPlan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        edit = findViewById(R.id.edit);

        container = findViewById(R.id.container);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        container.removeAllViews();
        if (hasPlan) {
            initPlanView();
        } else {
            View errorView = LayoutInflater.from(this).inflate(R.layout.layout_error, null, false);
            ImageView errorImg = errorView.findViewById(R.id.error_img);
            TextView errorText = errorView.findViewById(R.id.error_text);
            BaselineTextView errorAction = errorView.findViewById(R.id.error_action);
            Glide.with(this).load(R.mipmap.bg_plan_box).into(errorImg);
            errorText.setText("还没有计划噢~");
            errorAction.setText("去添加计划");
            errorAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "添加新计划");
                }
            });
            container.addView(errorView);
        }

    }

    //初始化planView
    private void initPlanView() {
        View planView = LayoutInflater.from(this).inflate(R.layout.plan_view, null, false);
        mPlanRv = planView.findViewById(R.id.recycler_view);
        wordTag = planView.findViewById(R.id.learn_tag);
        remain = planView.findViewById(R.id.learn_remain);
        expectedDay = planView.findViewById(R.id.plan_date);
        mPlans = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            PlanData data = new PlanData();
            if (i % 2 == 0) {
                data.setLearning(false);
                data.setTag("小学人教版一年级上");
                data.setWordCount(60 + "单词");
            } else {
                data.setLearning(true);
                data.setTag("初中外研版七年级下");
                data.setWordCount(680 + "单词");
                data.setProgress(30);
            }
            mPlans.add(data);
        }

        mAdapter = new PlanAdapter(this, mPlans);
        mPlanRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPlanRv.setAdapter(mAdapter);

        container.addView(planView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
