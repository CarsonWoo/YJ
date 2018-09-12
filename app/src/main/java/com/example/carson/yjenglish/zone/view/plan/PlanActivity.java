package com.example.carson.yjenglish.zone.view.plan;

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
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.PlanAdapter;
import com.example.carson.yjenglish.customviews.BaselineTextView;
import com.example.carson.yjenglish.customviews.PickerView;
import com.example.carson.yjenglish.utils.CalculateUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.PlanService;
import com.example.carson.yjenglish.zone.model.MyLearningPlanInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    private List<MyLearningPlanInfo.Data.WordInfo> mPlans = new ArrayList<>();
    private TextView wordTag;
    private TextView remain;
    private TextView expectedDay;

    private PickerView mWordPiker;
    private PickerView mDayPicker;

    private boolean hasPlan = false;

    private String mTag;
    private int mWordCount;

    private boolean isEditable = false;

    private List<String> words = new ArrayList<>();
    private List<String> days = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        executeGetMyPlanTask();
//        initViews();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void executeGetMyPlanTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        PlanService service = retrofit.create(PlanService.class);
        service.getMyLearningPlans(UserConfig.getToken(this))
                .enqueue(new Callback<MyLearningPlanInfo>() {
                    @Override
                    public void onResponse(Call<MyLearningPlanInfo> call, Response<MyLearningPlanInfo> response) {
                        MyLearningPlanInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            mPlans = info.getData().getHave_plan();
                            String selected = info.getData().getSelected_plan();
                            if ((mPlans != null && mPlans.size() == 0) || mPlans == null) {
                                hasPlan = false;
                                if (UserConfig.HasPlan(MyApplication.getContext())) {
                                    UserConfig.cacheHasPlan(MyApplication.getContext(), false);
                                }
                            } else {
                                hasPlan = true;
                                if (!UserConfig.HasPlan(MyApplication.getContext())) {
                                    UserConfig.cacheHasPlan(MyApplication.getContext(), true);
                                }
                                for (MyLearningPlanInfo.Data.WordInfo data : mPlans) {
                                    if (data.getPlan().equals(selected)) {
                                        data.setLearning(true);
                                        data.setProgress(0);
                                    } else {
                                        data.setLearning(false);
                                    }
                                }
                            }
                            initViews();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyLearningPlanInfo> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
    }

    private void initViews() {

        edit = findViewById(R.id.edit);

        container = findViewById(R.id.plan_container);
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
                    toAdd.putExtra("from_intent", PlanAddAty.INTENT_FROM_PLAN);
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

        edit.setVisibility(View.VISIBLE);
        edit.setText("编辑");

        mAdapter = new PlanAdapter(this, mPlans);
        mPlanRv.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mPlanRv.setAdapter(mAdapter);

        //先默认一开始进入的时候就选中第一项
        mTag = mPlans.get(0).getPlan();
        mWordCount = Integer.parseInt(mPlans.get(0).getWord_number());

        initPicker();

        mDayPicker.setScrollable(false);
        mWordPiker.setScrollable(false);
        wordTag.setText(mTag);
        remain.setText("（剩余" + mWordCount + "个单词）");
        expectedDay.setText(date2Str(CalculateUtils.getDateAfter(new Date(), Integer.parseInt(mDayPicker.
                getText().replace("天", "")))));

        //加载sharepreference的数据
        loadSaveSp();

        mAdapter.setChangeListener(new PlanAdapter.OnItemChangeListener() {
            @Override
            public void onCardClick(View view, String tag, String wordCount) {
                mTag = tag;
                mWordCount = Integer.parseInt(wordCount);
                wordTag.setText(mTag);
                remain.setText("（剩余" + mWordCount + "个单词）");
                initPicker();
                expectedDay.setText(date2Str(CalculateUtils.getDateAfter(new Date(), Integer.parseInt(mDayPicker.
                        getText().replace("天", "")))));
                loadSaveSp();
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
                    for (MyLearningPlanInfo.Data.WordInfo data : mPlans) {
                        data.setEditing(true);
                    }
                    mDayPicker.setScrollable(true);
                    mWordPiker.setScrollable(true);
                    edit.setText("确定");
                } else {
                    for (MyLearningPlanInfo.Data.WordInfo data : mPlans) {
                        data.setEditing(false);
                    }
                    //此时的text为确定 若按了确定
                    mDayPicker.setScrollable(false);
                    mWordPiker.setScrollable(false);
                    //post to server & save
                    SharedPreferences.Editor editor = getSharedPreferences(mTag, MODE_PRIVATE).edit();
                    editor.putString("plan_day", mDayPicker.getText());
                    editor.putString("plan_daily_count", mWordPiker.getText()).apply();
                    edit.setText("编辑");
                }
                isEditable = !isEditable;
                mAdapter.notifyDataSetChanged();
            }
        });

        container.addView(planView);
    }

    private void loadSaveSp() {
        SharedPreferences getPref = getSharedPreferences(mTag, MODE_PRIVATE);
//        Log.e(TAG, getPref.getString("plan_day", (mWordCount / 35) + "天"));
//        Log.e(TAG, getPref.getString("plan_daily_count", "35单词"));
        mDayPicker.setSelected(getPref.getString("plan_day", (mWordCount / 35) + "天"));
        mWordPiker.setSelected(getPref.getString("plan_daily_count", "35单词"));
    }

    private void initPicker() {
        days.clear();
        words.clear();
        for (int i = 10; i <= 60; i += 5) {
            days.add((mWordCount / i) + "天");
            words.add(i + "单词");
        }
        mDayPicker.setData(days);
        mWordPiker.setData(words);

        mWordPiker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                String str = (mWordCount / Integer.parseInt(text.replace("单词", ""))) + "天";
                mDayPicker.setSelected(str);
                remain.setText("（剩余" + mWordCount + "个单词）");
                Date afterDate = CalculateUtils.getDateAfter(new Date(), Integer.
                        parseInt(mDayPicker.getText().replace("天", "")));
                String dateStr = date2Str(afterDate);
                expectedDay.setText(dateStr);
            }
        });
        mDayPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                int i = mWordCount / Integer.parseInt(text.replace("天", ""));
                if (i % 5 != 0) {
                    i = (i / 5) * 5;
                }
                String str = i + "单词";
                mWordPiker.setSelected(str);
                Date afterDate = CalculateUtils.getDateAfter(new Date(), Integer.
                        parseInt(mDayPicker.getText().replace("天", "")));
                String dateStr = date2Str(afterDate);
                expectedDay.setText(dateStr);
            }
        });
    }

    private String date2Str(Date afterDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(afterDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            mAdapter.notifyDataSetChanged();
            executeGetMyPlanTask();
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
