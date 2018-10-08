package com.example.carson.yjenglish.home.view.word;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.RememberListAdapter;
import com.example.carson.yjenglish.home.WordService;
import com.example.carson.yjenglish.home.model.word.RememberWordInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by 84594 on 2018/9/2.
 */

public class RememberTab extends Fragment {

    private PullToRefreshRecyclerView recyclerView;
    private RememberListAdapter adapter;

    private List<RememberWordInfo.RememberWord> mList = new ArrayList<>();

    private Retrofit retrofit;

    private View view;

    private int refreshCount = 1;

    public static RememberTab newInstance() {
        RememberTab tab = new RememberTab();
        return tab;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.layout_pull_to_refresh_recyclerview, container, false);
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        executeRememberTask(String.valueOf(refreshCount));
//        initRecyclerView(view);
        return view;
    }

    private void executeRememberTask(final String page) {
        WordService service = retrofit.create(WordService.class);
        Call<RememberWordInfo> call = service.getRememberWords(UserConfig.getToken(getContext()),
                page, "10");
        call.enqueue(new Callback<RememberWordInfo>() {
            @Override
            public void onResponse(Call<RememberWordInfo> call, Response<RememberWordInfo> response) {
                RememberWordInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    if (page.equals("1")) {
                        //第一次加载10条
                        mList = info.getData();
                        initRecyclerView(view);
                    } else {
                        //后面加载的直接往后加
                        if (info.getData() != null && info.getData().size() > 0) {
                            mList.addAll(info.getData());
                        } else {
                            Toast.makeText(getContext(), "没有更多了...", Toast.LENGTH_SHORT).show();
                            recyclerView.setLoadingMoreEnabled(false);
                        }
                    }
                } else {
                    Log.e("RememberTab", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<RememberWordInfo> call, Throwable t) {
                Log.e("RememberTab", t.getMessage());
            }
        });
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(true);

        recyclerView.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                refreshCount++;
                executeRememberTask(String.valueOf(refreshCount));
                adapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setLoadMoreComplete();
                    }
                }, 800);
            }
        });

        adapter = new RememberListAdapter(getContext(), mList);
        recyclerView.setAdapter(adapter);

        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_error, null, false);
        ImageView errorImg = emptyView.findViewById(R.id.error_img);
        TextView errorText = emptyView.findViewById(R.id.error_text);

        Glide.with(getContext()).load(R.mipmap.bg_plan_box).thumbnail(0.8f).into(errorImg);
        errorText.setText("暂时没有更多噢...");

        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        recyclerView.setEmptyView(emptyView);


        /**
         * 还需添加按钮的响应 如按下了pass后
         */

    }

    public int getWordCount() {
        return mList.size();
    }

}
