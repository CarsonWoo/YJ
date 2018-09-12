package com.example.carson.yjenglish.zone.view.users;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.viewbinder.feeds.EmptyViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.zone.model.forviewbinder.DailyCardItem;
import com.example.carson.yjenglish.zone.model.forviewbinder.HomeFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.MusicItem;
import com.example.carson.yjenglish.zone.model.forviewbinder.TVFeeds;
import com.example.carson.yjenglish.zone.model.forviewbinder.UserActive;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordItem;
import com.example.carson.yjenglish.zone.viewbinder.EmptyBinder;
import com.example.carson.yjenglish.zone.viewbinder.UserActiveBinder;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class ActiveFragment extends Fragment {

    private OnActiveItemListener mListener;
    private RecyclerView recyclerView;

    private MultiTypeAdapter mAdapter;
    private Items mItems;

    private List<String> mList = new ArrayList<>();//进行模拟

    public ActiveFragment() {
        // Required empty public constructor
    }

    public static ActiveFragment newInstance() {
        return new ActiveFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        initData();
        initRecyclerViews(view);
        return view;
    }

    private void initData() {
        mList.add("ss");
    }

    private void initRecyclerViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new MultiTypeAdapter();
        mItems = new Items();
        mAdapter.setItems(mItems);
        recyclerView.setAdapter(mAdapter);
        if (mList.size() == 0) {
            mAdapter.register(String.class, new EmptyBinder());
            mItems.add("55555~暂时没有动态噢~");
        } else {
            mAdapter.register(UserActive.class, new UserActiveBinder());
            UserActive active = new UserActive(0, 0, "Carson",
                    "http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg");
            active.setHomeFeeds(new HomeFeeds("http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg",
                    "”从英“的365里路", "http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg",
                    "Julia", "刚刚"));
            mItems.add(active);
            active = new UserActive(1, 1, "Carson",
                    "http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg", "哎呦不错哦");
            active.setTvFeeds(new TVFeeds("http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg",
                    "26.3w", "universal", "/ju:ni'v3sal/", "18:23"));
            mItems.add(active);
            active = new UserActive(2, 0, "Carson",
                    "http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg");
            active.setMusicItem(new MusicItem("universal", "/ju:ni'v3sal/", "14:55"));
            mItems.add(active);
            active = new UserActive(3, 0, "Carson",
                    "http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg");
            active.setWordItem(new WordItem("car", "/cаr/", "12:10",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg"));
            mItems.add(active);
            active = new UserActive(4, 0, "Carson",
                    "http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg");
            active.setDailyCardItem(new DailyCardItem("http://cdn.duitang.com/uploads/item/201507/10/20150710045602_wHEBf.jpeg",
                    "09:23"));
            mItems.add(active);
            active = new UserActive(0, 2, "Carson",
                    "http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg",
                    "谢谢楼主分享咯~", "帅的被人砍");
            active.setHomeFeeds(new HomeFeeds("http://pic.qiantucdn.com/58pic/19/57/12/47B58PICxdD_1024.jpg",
                    "学英语有益身心健康", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2859719634,4239030051&fm=27&gp=0.jpg",
                    "阿姆", "昨天"));
            mItems.add(active);
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActiveItemListener) {
            mListener = (OnActiveItemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnActiveItemListener {
        void onItemClick();
    }
}
