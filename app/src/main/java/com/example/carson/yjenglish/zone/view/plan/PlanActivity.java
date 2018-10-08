package com.example.carson.yjenglish.zone.view.plan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.utils.CalculateUtils;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.PlanService;
import com.example.carson.yjenglish.zone.model.MyLearningPlanInfo;
import com.example.carson.yjenglish.zone.model.MyPlanDailyInfo;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

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

    private View planView;

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
    private List<String> dates = new ArrayList<>();

    private List<String> wordsCp = new ArrayList<>(), daysCp = new ArrayList<>();

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
                                        LitePal.getDatabase();
                                        LoadHeader loadData = DataSupport.where("header_id = ?", "1")
                                                .find(LoadHeader.class).get(0);
                                        data.setProgress((int) loadData.getProgress());
                                    } else {
                                        data.setLearning(false);
                                    }
                                }
                                //先对数据进行空数据化 因为至少要有一项才能加载pickerview
                                days.add("");
                                words.add("");
                                dates.add("");
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
        planView = LayoutInflater.from(this).inflate(R.layout.plan_view, null, false);
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
        mPlanRv.setItemAnimator(new DefaultItemAnimator());
        mPlanRv.setAdapter(mAdapter);

        //先默认一开始进入的时候就选中第一项
        mTag = mPlans.get(0).getPlan();
        mWordCount = Integer.parseInt(mPlans.get(0).getWord_number());

        mDayPicker.setData(days);
        mWordPiker.setData(words);
        executeLoadDailyTask(mTag);

        mDayPicker.setScrollable(false);
        mWordPiker.setScrollable(false);
        wordTag.setText(mTag);
        remain.setText("（剩余" + mWordCount + "个单词）");

        mAdapter.setChangeListener(new PlanAdapter.OnItemChangeListener() {
            @Override
            public void onCardClick(View view, String tag, String wordCount) {
                boolean isClicked = tag.equals(mTag);//判断当前的卡片是否与上次点击的一致 若一致则不再访问网络
                mTag = tag;
                mWordCount = Integer.parseInt(wordCount);
                wordTag.setText(mTag);
                remain.setText("（剩余" + mWordCount + "个单词）");
                if (!isClicked) {
                    executeLoadDailyTask(mTag);
                }
                int pos = daysCp.indexOf(mDayPicker.getText());
                expectedDay.setText(dates.get(pos));
            }

            @Override
            public void onAddClick(View view) {
                Intent toAdd = new Intent(PlanActivity.this, PlanAddAty.class);
                startActivityForResult(toAdd, 101);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
            }

            @Override
            public void onResetClick(View view, final String tag) {
                DialogUtils utils = DialogUtils.getInstance(PlanActivity.this);
                Dialog dialog = utils.newTipsDialog("确定重置" + tag + "吗",
                        View.TEXT_ALIGNMENT_CENTER);
                utils.setTipsListener(new DialogUtils.OnTipsListener() {
                    @Override
                    public void onConfirm() {
                        Toast.makeText(getApplicationContext(), "重置了" + tag,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

            }

            @Override
            public void onDeleteClick(View view, String tag, int pos) {
                executeDeleteTask(tag, pos);
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

                    Log.e(TAG, "mTag = " + mTag);
                    Log.e(TAG, "cachePlan = " + UserConfig.getSelectedPlan(PlanActivity.this));
                    if (!mTag.equals(UserConfig.getSelectedPlan(PlanActivity.this))) {
                        //如果当前选择的计划不是正在学习的计划
                        executeSelectedPlanTask();
                    } else {
                        executeChangePlanTask();
                    }

                    //此时的text为确定 若按了确定
                    mDayPicker.setScrollable(false);
                    mWordPiker.setScrollable(false);

                    //post to server & save
//                    SharedPreferences.Editor editor = getSharedPreferences(mTag, MODE_PRIVATE).edit();
//                    editor.putString("plan_day", mDayPicker.getText());
//                    editor.putString("plan_daily_count", mWordPiker.getText()).apply();
                }
                isEditable = !isEditable;
                mAdapter.notifyDataSetChanged();
            }
        });

        container.addView(planView);
    }

    private void executeSelectedPlanTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        PlanService service = retrofit.create(PlanService.class);
        service.setSelectedPlan(UserConfig.getToken(this), mTag).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    UserConfig.cacheSelectedPlan(PlanActivity.this, mTag);
                    executeChangePlanTask();
                } else {
//                    Toast.makeText(PlanActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Toast.makeText(PlanActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void executeChangePlanTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        PlanService service = retrofit.create(PlanService.class);
        service.changePlanDaily(UserConfig.getToken(this),
                mWordPiker.getText().replace("单词", ""),
                mDayPicker.getText().replace("天", "")).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    edit.setText("编辑");
                    sendBroadcast(new Intent("HEADER_CHANGE"));
                    Toast.makeText(PlanActivity.this, "更改计划成功", Toast.LENGTH_SHORT).show();
                    for (MyLearningPlanInfo.Data.WordInfo data : mPlans) {
                        if (data.getPlan().equals(UserConfig.getSelectedPlan(MyApplication.getContext()))) {
                            data.setLearning(true);
                            LitePal.getDatabase();
                            LoadHeader loadData = DataSupport.where("header_id = ?", "1")
                                    .find(LoadHeader.class).get(0);
                            data.setProgress((int) loadData.getProgress());
                        } else {
                            data.setLearning(false);
                        }
                    }
                    SharedPreferences.Editor editor = getSharedPreferences(mTag, MODE_PRIVATE).edit();
                    editor.putString("plan_day", mDayPicker.getText());
                    editor.putString("plan_daily_count", mWordPiker.getText()).apply();
                    mAdapter.notifyDataSetChanged();
                } else {
//                    Toast.makeText(PlanActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void executeLoadDailyTask(final String tag) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        PlanService service = retrofit.create(PlanService.class);
        service.getPlanDaily(UserConfig.getToken(this), tag).enqueue(new Callback<MyPlanDailyInfo>() {
            @Override
            public void onResponse(Call<MyPlanDailyInfo> call, Response<MyPlanDailyInfo> response) {
                MyPlanDailyInfo info = response.body();
                days.clear();
                words.clear();
                dates.clear();
                daysCp.clear();
                wordsCp.clear();
                if (info.getStatus().equals("200")) {
                    for (MyPlanDailyInfo.MyPlanDaily daily : info.getData()) {
                        days.add(daily.getDays() + "天");
                        words.add(daily.getDaily_word_number() + "单词");
                        dates.add(daily.getDate());
                        daysCp.add(daily.getDays() + "天");
                        wordsCp.add(daily.getDaily_word_number() + "单词");
                    }
                } else {
                    days.add("-天");
                    words.add("-单词");
                    dates.add("-");
                }

                mWordPiker.setData(words);
                mDayPicker.setData(days);
                initPicker();
            }

            @Override
            public void onFailure(Call<MyPlanDailyInfo> call, Throwable t) {
//                Log.e(TAG, t.getMessage());
                Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void executeDeleteTask(final String tag, final int pos) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        PlanService service = retrofit.create(PlanService.class);
        service.deletePlan(UserConfig.getToken(this), tag).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    mPlans.remove(pos);
                    mAdapter.notifyItemRemoved(pos);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 500);
                } else {
                    Log.e(TAG, info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void loadSaveSp() {
        SharedPreferences getPref = getSharedPreferences(mTag, MODE_PRIVATE);
//        Log.e(TAG, getPref.getString("plan_day", (mWordCount / 35) + "天"));
//        Log.e(TAG, getPref.getString("plan_daily_count", "35单词"));
        mDayPicker.setSelected(getPref.getString("plan_day", days.get(days.size() / 2)));
        mWordPiker.setSelected(getPref.getString("plan_daily_count", words.get(words.size() / 2)));
    }

    private void initPicker() {
        loadSaveSp();
        mWordPiker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                int position = wordsCp.indexOf(text);
                mDayPicker.setSelected(daysCp.get(position));
                remain.setText("（剩余" + mWordCount + "个单词）");
                expectedDay.setText(dates.get(position));
            }
        });
        mDayPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                int position = daysCp.indexOf(text);
                mWordPiker.setSelected(wordsCp.get(position));
                expectedDay.setText(dates.get(position));
            }
        });
    }

    private String date2Str(Date afterDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(afterDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            mAdapter.notifyDataSetChanged();
            executeGetMyPlanTask();
            sendBroadcast(new Intent("HEADER_CHANGE"));
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
