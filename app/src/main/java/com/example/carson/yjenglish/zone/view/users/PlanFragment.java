package com.example.carson.yjenglish.zone.view.users;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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
import com.example.carson.yjenglish.zone.model.OthersPlan;
import com.example.carson.yjenglish.zone.model.forviewbinder.OtherUsersPlan;
import com.example.carson.yjenglish.zone.viewbinder.EmptyBinder;
import com.example.carson.yjenglish.zone.viewbinder.OtherUsersPlanBinder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment {

    private static final String MY_PLANS = "my_plans";

    private RecyclerView recyclerView;
    private List<MyLearningPlanInfo.Data.WordInfo> mPlans;

    private MultiTypeAdapter mAdapter;
    private Items mItems;

    private List<String> mList = new ArrayList<>();//进行模拟

    private List<OthersPlan> plans;

    public PlanFragment() {
        // Required empty public constructor
    }

    public static PlanFragment newInstance(List<OthersPlan> mList) {
        PlanFragment fragment = new PlanFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MY_PLANS, (ArrayList<? extends Parcelable>) mList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mPlans = getArguments().getString(MY_PLANS);
            plans = getArguments().getParcelableArrayList(MY_PLANS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
//        mList.add("");
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

        if (plans.size() == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter.register(String.class, new EmptyBinder());
            mItems.add("55555~暂时没有计划噢~");
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            mAdapter.register(OtherUsersPlan.class, new OtherUsersPlanBinder());
            for (int i = 0; i < plans.size(); i++) {
                mItems.add(new OtherUsersPlan(plans.get(i).getPlan(), plans.get(i).getWord_number(),
                        Integer.parseInt(plans.get(i).getLearned_word_number()) * 100 /
                                Integer.parseInt(plans.get(i).getWord_number())));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

}
