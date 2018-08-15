package com.example.carson.yjenglish.discover;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.DiscoverAdapters;
import com.example.carson.yjenglish.customviews.CanotSlidingViewPager;
import com.example.carson.yjenglish.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    private CanotSlidingViewPager viewPager;
    private PullToRefreshRecyclerView rvAty, rvGame;

    private List<String> imgUrls;
    private List<String> mList;

    private DiscoverAdapters.AtyAdapter mAdapter;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        viewPager = view.findViewById(R.id.vp);
        rvAty = view.findViewById(R.id.rv_aty);
        rvGame = view.findViewById(R.id.rv_game);

        imgUrls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            imgUrls.add("http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg");
        }

        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(70);
//        viewPager.setScrollable(false);
        viewPager.setCurrentItem(9);
//        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//        params.width = ScreenUtils.getScreenWidth(getContext()) - ScreenUtils.dp2px(getContext(), 80);
//        viewPager.setLayoutParams(params);
//        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));

        mList = new ArrayList<>();

        mAdapter = new DiscoverAdapters.AtyAdapter(getActivity(), mList);
        rvGame.setAdapter(mAdapter);
        rvAty.setAdapter(mAdapter);
        rvAty.setHasFixedSize(true);
        rvGame.setHasFixedSize(true);

        initRecycler(rvGame, "暂时还没运营噢...", R.drawable.testimg);
        initRecycler(rvAty, "暂时没有更多活动噢\n小语正在努力筹划~", R.drawable.testimg);

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

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.discover_daily_card,
                    null, false);
            ImageView bg = itemView.findViewById(R.id.bg_daily_card);
            ImageView like = itemView.findViewById(R.id.like);
            ImageView download = itemView.findViewById(R.id.download);
            ImageView share = itemView.findViewById(R.id.share);
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
            Glide.with(getContext()).load(imgUrls.get(position)).thumbnail(0.8f).into(bg);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
