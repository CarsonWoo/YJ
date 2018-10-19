package com.example.carson.yjenglish.msg.view;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.MsgAdapters;
import com.example.carson.yjenglish.msg.GetFavoursContract;
import com.example.carson.yjenglish.msg.GetFavoursTask;
import com.example.carson.yjenglish.msg.MsgService;
import com.example.carson.yjenglish.msg.model.FavoursInfo;
import com.example.carson.yjenglish.msg.model.LikeMsg;
import com.example.carson.yjenglish.msg.presenter.GetFavoursPresenter;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by 84594 on 2018/8/6.
 */

public class LikeFragment extends Fragment implements GetFavoursContract.View {

    private static final String TAG = "LikeFragment";
    private static LikeFragment INSTANCE = null;

    private PullToRefreshRecyclerView recyclerView;
    private List<LikeMsg> mLikes;
    private MsgAdapters.LikeAdapter mAdapter;

    private Dialog mDialog;

    private GetFavoursContract.Presenter presenter;

    public LikeFragment() {}

    public static LikeFragment getInstance () {
        if (INSTANCE == null) {
            INSTANCE = new LikeFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        mDialog = DialogUtils.getInstance(getContext()).newCommonDialog("信息读取中",
                R.mipmap.gif_loading_video, true);
        executeLoadTask();
        return view;
    }

    private void executeLoadTask() {
        GetFavoursTask task = GetFavoursTask.getInstance();
        GetFavoursPresenter favoursPresenter = new GetFavoursPresenter(task, this);
        this.setPresenter(favoursPresenter);
        presenter.getInfo(UserConfig.getToken(getContext()));
    }

    private void initRecyclerView() {
//        for (int i = 0; i < 10; i++) {
//            LikeMsg like = new LikeMsg();
//            like.setUsername("帅的被人砍");
//            like.setContent("帅哥的文章就是不一样");
//            like.setDate("刚刚");
//            like.setPortraitUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg");
//            mLikes.add(like);
//        }

        mAdapter = new MsgAdapters.LikeAdapter(getContext(), mLikes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        View emptyView = View.inflate(getContext(), R.layout.layout_error, null);
        TextView errorMsg = emptyView.findViewById(R.id.error_text);
        errorMsg.setText("暂时没有人点赞噢...");
        ImageView errorImg = emptyView.findViewById(R.id.error_img);
        Glide.with(getContext()).load(R.mipmap.bg_like_box).thumbnail(0.8f).into(errorImg);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setEmptyView(emptyView);

        recyclerView.setLoadingMoreEnabled(false);

        recyclerView.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onRefresh() {
//                mAdapter.notifyDataSetChanged();
//                executeLoadTask();
                refreshList();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        recyclerView.setRefreshComplete();
//                    }
//                }, 1000);
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    private void refreshList() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(MsgService.class).refreshFavoursInfo(UserConfig.getToken(getContext()))
                .enqueue(new Callback<FavoursInfo>() {
                    @Override
                    public void onResponse(Call<FavoursInfo> call, Response<FavoursInfo> response) {
                        FavoursInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            mLikes = info.getData();
                            mAdapter.notifyDataSetChanged();
                            recyclerView.setRefreshComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<FavoursInfo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void setPresenter(GetFavoursContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        mDialog.show();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(getContext(), 260);
        lp.height = ScreenUtils.dp2px(getContext(), 240);
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onSuccess(FavoursInfo info) {
        if (info.getStatus().equals("200")) {
            mLikes = info.getData();
            initRecyclerView();
        }
    }

    @Override
    public boolean isViewAdded() {
        return isAdded();
    }


}
