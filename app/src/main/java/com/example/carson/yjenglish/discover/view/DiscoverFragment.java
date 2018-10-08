package com.example.carson.yjenglish.discover.view;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.ImageShowActivity;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.DiscoverAdapters;
import com.example.carson.yjenglish.customviews.CanotSlidingViewPager;
import com.example.carson.yjenglish.discover.DiscoverInfoContract;
import com.example.carson.yjenglish.discover.DiscoverInfoTask;
import com.example.carson.yjenglish.discover.DiscoverService;
import com.example.carson.yjenglish.discover.model.DailyCardInfo;
import com.example.carson.yjenglish.discover.model.DiscoverInfo;
import com.example.carson.yjenglish.discover.presenter.DiscoverInfoPresenter;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment implements DiscoverInfoContract.View {

    private CanotSlidingViewPager viewPager;
    private PullToRefreshRecyclerView rvAty, rvGame;

//    private List<String> imgUrls = new ArrayList<>();
//    private List<Integer> imgUrls = new ArrayList<>();
    private List<DiscoverInfo.DiscoverItem.DailyCard> mDailyCards;
    private List<DiscoverInfo.DiscoverItem.WelfareService> mList = new ArrayList<>();

    private View view;

    private Dialog mDialog;

    private MyPagerAdapter mPagerAdapter;

    private DiscoverAdapters.AtyAdapter mAdapter;

    private DiscoverInfoContract.Presenter mPresenter;
    private DiscoverInfoPresenter infoPresenter;

    private int mPagePosition;

    private int mLoadingPage = 2;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);
        mDialog = DialogUtils.getInstance(getContext()).newCommonDialog("加载中", R.mipmap.gif_loading_video, true);
//        initViews(view);
        bindViews();
        executeLoadingTask();
        return view;
    }

    private void bindViews() {
        viewPager = view.findViewById(R.id.vp);
        rvAty = view.findViewById(R.id.rv_aty);
        rvGame = view.findViewById(R.id.rv_game);
    }

    private void executeLoadingTask() {
        DiscoverInfoTask task = DiscoverInfoTask.getInstance();
        infoPresenter = new DiscoverInfoPresenter(task, this);
        this.setPresenter(infoPresenter);
        mPresenter.getInfo(UserConfig.getToken(getContext()));
    }

    private void initViews() {

        mPagerAdapter = new MyPagerAdapter();

        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(70);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mDailyCards.size() - 1) {
                    flushData();
                }
//                if (positionOffsetPixels == 0 && positionOffset == 0){
//                    //在这里面刷新数据
//                    flushData();
//                }
            }

            @Override
            public void onPageSelected(int position) {
                mPagePosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(mDailyCards.size() - 1);
        viewPager.setFocusable(true);
        viewPager.setFocusableInTouchMode(true);
//        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//        params.width = ScreenUtils.getScreenWidth(getContext()) - ScreenUtils.dp2px(getContext(), 80);
//        viewPager.setLayoutParams(params);
//        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));

        mAdapter = new DiscoverAdapters.AtyAdapter(getActivity(), mList);
        rvAty.setAdapter(mAdapter);
        rvAty.setHasFixedSize(true);
        initRecycler(rvAty, "暂时没有更多活动噢\n小语正在努力筹划~", R.mipmap.bg_plan_box);

//        rvGame.setAdapter(mAdapter);
//        rvGame.setHasFixedSize(true);
        initRecycler(rvGame, "暂时还没运营噢...", R.mipmap.bg_plan_box);

    }

    private void flushData() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(DiscoverService.class).getMorePics(UserConfig.getToken(getContext()),
                String.valueOf(mLoadingPage), "6").enqueue(new Callback<DailyCardInfo>() {
            @Override
            public void onResponse(Call<DailyCardInfo> call, Response<DailyCardInfo> response) {
                DailyCardInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    if (!info.getData().isEmpty()) {
                        mLoadingPage ++;
                        mDailyCards.addAll(info.getData());
                        mPagerAdapter.notifyDataSetChanged();
                    } else {
//                        DiscoverInfo.DiscoverItem.DailyCard card = mDailyCards.get(0);
//                        card.setIs_favour("1");
//                        mDailyCards.add(0, card);
//                        mPagerAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "没有更多咯~", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Discover", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<DailyCardInfo> call, Throwable t) {
                Toast.makeText(getContext(), "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecycler(PullToRefreshRecyclerView rv, String text, int drawableRes) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setLoadingMoreEnabled(false);
        rv.setPullRefreshEnabled(false);
        rv.setNestedScrollingEnabled(false);
        View emptyView = View.inflate(getContext(), R.layout.layout_error_card, null);
        TextView tv = emptyView.findViewById(R.id.error_text);
        ImageView iv = emptyView.findViewById(R.id.error_img);
        tv.setText(text);
        Glide.with(getContext()).load(drawableRes).thumbnail(0.5f).into(iv);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        emptyView.setLayoutParams(params);
        rv.setEmptyView(emptyView);
    }

    @Override
    public void setPresenter(DiscoverInfoContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(DiscoverInfo info) {
        if (info.getStatus().equals("200")) {
            mDailyCards = info.getData().getDaily_pic();

            mList = info.getData().getWelfare_service();
            initViews();
        } else {
            Log.e("Discover", info.getMsg());
        }
    }


    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mDailyCards.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.discover_daily_card,
                    container, false);

            itemView.setTag(position);

            ImageView bg = itemView.findViewById(R.id.bg_daily_card);
            ImageView like = itemView.findViewById(R.id.like);
            ImageView download = itemView.findViewById(R.id.download);
            ImageView share = itemView.findViewById(R.id.share);

            like.setSelected(mDailyCards.get(position).getIs_favour().equals("1"));

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Dis", "like " + position);
                }
            });
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("Dis", "download " + position);
                }
            });

            bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toImg = new Intent(getContext(), ImageShowActivity.class);
                    toImg.putExtra("img_url", mDailyCards.get(position).getDaily_pic());
                    startActivity(toImg);
                }
            });

            Glide.with(getContext()).load(mDailyCards.get(position).getDaily_pic()).thumbnail(0.7f).into(bg);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            View view = (View) object;
            int currentPage = mPagePosition;
            if (currentPage == (Integer) view.getTag()) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }
    }
}
