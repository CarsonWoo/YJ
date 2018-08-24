package com.example.carson.yjenglish.music.view;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 84594 on 2018/8/23.
 */

public class MyDialogFragment extends DialogFragment {


    private RememberTab tabRemember;
    private LikeTab tabLike;
    private DownloadTab tabDownload;

    public MyDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        //去出标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        View inflate = inflater.inflate(R.layout.layout_music_container, container, false);
        initViews(inflate);
        return inflate;
    }

    private void initViews(View windowView) {
        final ImageView rememImg = windowView.findViewById(R.id.tab_remember_img);
        final ImageView likeImg = windowView.findViewById(R.id.tab_like_img);
        final ImageView downloadImg = windowView.findViewById(R.id.tab_download_img);
        final TextView rememTxt = windowView.findViewById(R.id.tab_remember_text);
        final TextView likeTxt = windowView.findViewById(R.id.tab_like_text);
        final TextView downloadTxt = windowView.findViewById(R.id.tab_download_text);

        final ViewPager mViewPager = windowView.findViewById(R.id.music_view_pager);

        MyViewPagerAdapter mAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);

        rememImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });

        likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        downloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetRes();
                switch (position) {
                    case 0:
                        rememImg.setSelected(true);
                        rememTxt.setTextColor(Color.BLACK);
                        break;
                    case 1:
                        likeImg.setSelected(true);
                        likeTxt.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        downloadImg.setSelected(true);
                        downloadTxt.setTextColor(Color.BLACK);
                        break;
                }
            }

            private void resetRes() {
                rememImg.setSelected(false);
                likeImg.setSelected(false);
                downloadImg.setSelected(false);
                rememTxt.setTextColor(Color.DKGRAY);
                likeTxt.setTextColor(Color.DKGRAY);
                downloadTxt.setTextColor(Color.DKGRAY);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable( new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = (int) (ScreenUtils.getScreenHeight(getContext()) * 0.6);
        win.setAttributes(params);
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (tabRemember == null) {
                        tabRemember = RememberTab.newInstance();
                    }
                    return tabRemember;
                case 1:
                    if (tabLike == null) {
                        tabLike = LikeTab.newInstance();
                    }
                    return tabLike;
                case 2:
                    if (tabDownload == null) {
                        tabDownload = DownloadTab.newInstance();
                    }
                    return tabDownload;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
