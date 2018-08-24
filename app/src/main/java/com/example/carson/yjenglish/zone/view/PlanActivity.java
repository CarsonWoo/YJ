package com.example.carson.yjenglish.zone.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.PlanAdapter;
import com.example.carson.yjenglish.customviews.BaselineTextView;
import com.example.carson.yjenglish.customviews.PickerView;
import com.example.carson.yjenglish.zone.PlanAddAty;
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

    private PickerView mWordPiker;
    private PickerView mDayPicker;

    private boolean hasPlan = true;

    private String mTag;
    private SharedPreferences saveSp;

    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        edit = findViewById(R.id.edit);

        container = findViewById(R.id.plan_container);

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
                    Intent toAdd = new Intent(PlanActivity.this, PlanAddAty.class);
                    startActivityForResult(toAdd, 102);
                    overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
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
        mWordPiker = planView.findViewById(R.id.picker_word);
        mDayPicker = planView.findViewById(R.id.picker_day);
        mPlans = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            PlanData data = new PlanData();
            if (i % 2 == 0) {
                data.setLearning(false);
                data.setTag("小学人教版一年级上" + i);
                data.setWordCount(60 + "单词");
            } else {
                data.setLearning(true);
                data.setTag("初中外研版七年级下" + i);
                data.setWordCount(680 + "单词");
                data.setProgress(30);
            }
            mPlans.add(data);
        }

        edit.setVisibility(View.VISIBLE);
        edit.setText("编辑");

        mAdapter = new PlanAdapter(this, mPlans);
        mPlanRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPlanRv.setAdapter(mAdapter);

        final List<String> words = new ArrayList<>();
        final List<String> days = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            days.add((i + 1) * 3 + "天");
            words.add((i + 1) * 10 + "个单词");
        }
        mWordPiker.setData(words);
        mDayPicker.setData(days);
        final int mPlanDayIndex = days.size() / 2;
        final int mPlanCountIndex = words.size() / 2;

        //先默认一开始进入的时候就选中第一项
        mTag = mPlans.get(0).getTag();
        saveSp = getSharedPreferences(mTag, MODE_PRIVATE);

        mDayPicker.setSelected(saveSp.getString("dayStr", "18天"));
        mWordPiker.setSelected(saveSp.getString("countStr", "60个单词"));

        mWordPiker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                //作存储工作
                saveSp = getSharedPreferences(mTag, MODE_PRIVATE);
                saveSp.edit().putString("countStr", text).apply();
            }
        });
        mDayPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                saveSp = getSharedPreferences(mTag, MODE_PRIVATE);
                saveSp.edit().putString("dayStr", text).apply();
            }
        });

        mAdapter.setChangeListener(new PlanAdapter.OnItemChangeListener() {
            @Override
            public void onCardClick(View view, String tag) {
                mTag = tag;
                SharedPreferences getPref = getSharedPreferences(mTag, MODE_PRIVATE);
                mDayPicker.setSelected(getPref.getString("dayStr", "18天"));
                mWordPiker.setSelected(getPref.getString("countStr", "60个单词"));
            }

            @Override
            public void onAddClick(View view) {
                Intent toAdd = new Intent(PlanActivity.this, PlanAddAty.class);
                startActivityForResult(toAdd, 101);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
            }

            @Override
            public void onResetClick(View view, int pos) {
                Toast.makeText(getApplicationContext(), "重置了位置 " + (pos + 1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(View view, int pos) {
                Toast.makeText(getApplicationContext(), "删除了位置 " + (pos + 1), Toast.LENGTH_SHORT).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditable) {
                    for (PlanData data : mPlans) {
                        data.setEditing(true);
                    }
                    edit.setText("确定");
                } else {
                    for (PlanData data : mPlans) {
                        data.setEditing(false);
                    }
                    edit.setText("编辑");
                }
                isEditable = !isEditable;
                mAdapter.notifyDataSetChanged();
            }
        });

        container.addView(planView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            mAdapter.notifyDataSetChanged();
        } else if (requestCode == 102 && resultCode == RESULT_OK) {
            container.removeAllViews();
            initPlanView();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
