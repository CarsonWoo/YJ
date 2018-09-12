package com.example.carson.yjenglish.zone.view.plan;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.view.HomeFragment;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.PlanGetContract;
import com.example.carson.yjenglish.zone.PlanService;
import com.example.carson.yjenglish.zone.model.PlanInfo;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordTag;
import com.example.carson.yjenglish.zone.viewbinder.WordTagViewBinder;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrimaryTab extends Fragment implements PlanGetContract.View,
        WordTagViewBinder.OnTagClickListener{

    private RecyclerView recyclerView;
    private MultiTypeAdapter mAdapter;

    private Items items;

    private Dialog mDialog;

    private PlanGetContract.Presenter mPresenter;

    private List<PlanInfo.Data> mList = new ArrayList<>();

    public PrimaryTab() {
        // Required empty public constructor
    }

    public static PrimaryTab newInstance() {
        PrimaryTab primaryFragment = new PrimaryTab();
        return primaryFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDialog = new ProgressDialog(getActivity());
        mDialog.setTitle("加载中");
        mPresenter.setInfo("小学");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        initRecyclerViews(view);
        return view;
    }

    private void initRecyclerViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        items = new Items();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (items.get(position) instanceof WordTag) {
                    return 1;
                } else {
                    return 3;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new MultiTypeAdapter(items);
        mAdapter.register(EmptyValue.class, new FieldTitleViewBinder());
        mAdapter.register(WordTag.class, new WordTagViewBinder(this));

        recyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        for (int i = 0; i < mList.size(); i++) {
            items.add(new WordTag(mList.get(i).getPlan(),
                    mList.get(i).getWord_number() + "单词"));
        }
        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(PlanGetContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mDialog.isShowing()) mDialog.dismiss();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void getInfo(PlanInfo info) {
        if (info.getStatus().equals("200")) {
            mList = info.getData();
            loadData();
        } else {
            Log.e("Pri", info.getMsg());
        }
    }

    @Override
    public void onTagClick(final String tag, int pos) {
        DialogUtils dialogUtils = DialogUtils.getInstance(getActivity());
        final Dialog mDialog = dialogUtils.newPickerDialog(tag, mList.get(pos).getWord_number());
        dialogUtils.show(mDialog);
        dialogUtils.setPickerListener(new DialogUtils.OnPickerListener() {
            @Override
            public void onConfirm(String day, String count, String date) {
                executeAddTask(tag, day.replace("天", ""), count.replace("单词", ""));
                mDialog.dismiss();
            }
        });
    }

    private void executeAddTask(String tag, String day, String count) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        PlanService service = retrofit.create(PlanService.class);
        Call<CommonInfo> call = service.addPlan(UserConfig.getToken(getContext()),
                tag, day, count);
        call.enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    Toast.makeText(getContext(), "添加计划成功", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        if (!UserConfig.HasPlan(getContext())) {
                            UserConfig.cacheHasPlan(getContext(), true);
                        }
                        if (PlanAddAty.fromIntent == PlanAddAty.INTENT_FROM_PLAN) {
                            getActivity().setResult(Activity.RESULT_OK);
                        } else {
                            getActivity().setResult(HomeFragment.RESULT_ADD_PLAN_OK);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), info.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
}
