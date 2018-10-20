package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 84594 on 2018/9/12.
 */

public class MultiPortraitView extends FrameLayout {

    private List<Integer> mResList = new ArrayList<>();
    private List<String> mUrlList;

    private LoadMorePortraitListener listener;

    private List<CircleImageView> mPortraits;

    public MultiPortraitView(@NonNull Context context) {
        this(context, null);
    }

    public MultiPortraitView(Context context, List<String> mUrlList) {
        super(context);
        this.mUrlList = mUrlList;
        init(context);
    }

    private void init(Context context) {

        mPortraits = new ArrayList<>();
        View mView = LayoutInflater.from(context).inflate(R.layout.layout_multi_portrait_view, this,
                true);
        CircleImageView p1 = mView.findViewById(R.id.portrait_1);
        CircleImageView p2 = mView.findViewById(R.id.portrait_2);
        CircleImageView p3 = mView.findViewById(R.id.portrait_3);
        CircleImageView p4 = mView.findViewById(R.id.portrait_4);
        CircleImageView p5 = mView.findViewById(R.id.portrait_5);
        CircleImageView p6 = mView.findViewById(R.id.portrait_6);
        CircleImageView p7 = mView.findViewById(R.id.portrait_7);
        CircleImageView loadMore = mView.findViewById(R.id.portrait_load_more);

        mPortraits.add(p1);
        mPortraits.add(p2);
        mPortraits.add(p3);
        mPortraits.add(p4);
        mPortraits.add(p5);
        mPortraits.add(p6);
        mPortraits.add(p7);

        loadMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.loadMorePortraits();
                }
            }
        });

        mResList.add(R.mipmap.ic_music);
        mResList.add(R.mipmap.ic_music_download_yello);
        mResList.add(R.mipmap.bg_like_box);
        mResList.add(R.mipmap.home_header_img);
        mResList.add(R.mipmap.ic_delete_item_gray);
        mResList.add(R.mipmap.ic_download_with_background);
        mResList.add(R.drawable.zone_bg);

//        if (mUrlList == null) {
//            throw new RuntimeException("no list bind to the portrait");
//        }

        if (mUrlList != null && mUrlList.size() != 0) {
            for (int i = 0; i < mPortraits.size(); i++) {
                Glide.with(context).load(mUrlList.get(i)).thumbnail(0.5f).placeholder(R.mipmap.loading)
                .dontAnimate().fitCenter().into(mPortraits.get(i));
            }
        } else {
            Log.e("MultiPortrait", "urls is null");
            for (int i = 0; i < mPortraits.size(); i++) {
                Glide.with(context).load(mResList.get(i)).thumbnail(0.3f).fitCenter().into(mPortraits.get(i));
            }
        }
    }

    public void setListener(LoadMorePortraitListener listener) {
        this.listener = listener;
    }

    public void setmList(List<String> mUrlList) {
        this.mUrlList = mUrlList;
    }

    public interface LoadMorePortraitListener {
        void loadMorePortraits();
    }
}
