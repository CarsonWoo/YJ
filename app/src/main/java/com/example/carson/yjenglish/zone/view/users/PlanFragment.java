package com.example.carson.yjenglish.zone.view.users;


import android.os.Bundle;
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
import com.example.carson.yjenglish.adapter.PlanAdapter;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;
import com.example.carson.yjenglish.home.viewbinder.feeds.EmptyViewBinder;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.zone.model.MyLearningPlanInfo;
import com.example.carson.yjenglish.zone.model.forviewbinder.OtherUsersPlan;
import com.example.carson.yjenglish.zone.viewbinder.EmptyBinder;
import com.example.carson.yjenglish.zone.viewbinder.OtherUsersPlanBinder;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<MyLearningPlanInfo.Data.WordInfo> mPlans;


    private MultiTypeAdapter mAdapter;
    private Items mItems;

    private List<String> mList = new ArrayList<>();//进行模拟

    public PlanFragment() {
        // Required empty public constructor
    }

    public static PlanFragment newInstance() {
        return new PlanFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mList.add("");
        initRecycler(view);
        return view;
    }

    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        mPlans = new ArrayList<>();
        mAdapter = new MultiTypeAdapter();
        mItems = new Items();
        mAdapter.setItems(mItems);
        recyclerView.setAdapter(mAdapter);

        if (mList.size() == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter.register(String.class, new EmptyBinder());
            mItems.add("55555~暂时没有计划噢~");
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            mAdapter.register(OtherUsersPlan.class, new OtherUsersPlanBinder());
            mItems.add(new OtherUsersPlan("英语四级", "3460单词", 20));
            mItems.add(new OtherUsersPlan("英语六级", "1285单词", 0));
            mItems.add(new OtherUsersPlan("四级高频词汇", "1088单词", 40));
        }
        mAdapter.notifyDataSetChanged();
    }

}
