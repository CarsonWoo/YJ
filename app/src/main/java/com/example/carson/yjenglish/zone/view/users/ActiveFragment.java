package com.example.carson.yjenglish.zone.view.users;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.ZoneService;
import com.example.carson.yjenglish.zone.model.UserActiveInfo;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActiveFragment extends Fragment {

    private static final int TYPE_FAVOUR = 0;
    private static final int TYPE_COMMENT = 1;
    private static final int TYPE_REPLY = 2;

    private static final String FAVOUR = "favour";
    private static final String COMMENT = "comment";
    private static final String WORD = "word";
    private static final String VIDEO = "video";
    private static final String FEEDS = "feeds";
    private static final String DAILY_PIC = "daily_pic";

    private OnActiveItemListener mListener;
    private RecyclerView recyclerView;

    private MultiTypeAdapter mAdapter;
    private Items mItems;

    private List<String> mList = new ArrayList<>();//进行模拟
    private String user_id;//到时需要驱替

    private boolean isOpen;

    private String insist_day;

    private String username;

    private String portrait;

    private List<UserActiveInfo.ActiveInfo.ItemInfo> mInfos;

    public ActiveFragment() {
        // Required empty public constructor
    }

    public static ActiveFragment newInstance(String user_id) {
        ActiveFragment fragment = new ActiveFragment();
        Bundle args = new Bundle();
        args.putString("user_id", user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getString("user_id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
//        initData();
        recyclerView = view.findViewById(R.id.recycler_view);
        executeLoadTask();
//        initRecyclerViews(view);
        return view;
    }

    private void executeLoadTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(ZoneService.class).getActiveInfo(UserConfig.getToken(MyApplication.getContext()),
                user_id)
                .enqueue(new Callback<UserActiveInfo>() {
                    @Override
                    public void onResponse(Call<UserActiveInfo> call, Response<UserActiveInfo> response) {
                        UserActiveInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            mInfos = info.getData().getIts_dynamic();
                            isOpen = info.getData().getIs_open().equals("1");
                            insist_day = info.getData().getInsist_day();
                            username = info.getData().getUsername();
                            portrait = info.getData().getPortrait();

                            if (mListener != null) {
                                mListener.onChange(insist_day);
                            }

                            initRecyclerViews();

                        }
                    }

                    @Override
                    public void onFailure(Call<UserActiveInfo> call, Throwable t) {

                    }
                });
    }
//
//    private void initData() {
//        mList.add("ss");
//    }

    private void initRecyclerViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new MultiTypeAdapter();
        mItems = new Items();
        mAdapter.setItems(mItems);
        recyclerView.setAdapter(mAdapter);
        if (mInfos.size() == 0) {
            mAdapter.register(String.class, new EmptyBinder());
            mItems.add("55555~暂时没有动态噢~");
        } else {
            if (isOpen) {
                mAdapter.register(UserActive.class, new UserActiveBinder());
                initType();
            } else {
                mAdapter.register(String.class, new EmptyBinder());
                mItems.add(username + "没有开放动态噢");
            }
        }
        mAdapter.notifyDataSetChanged();

    }

    private void initType() {
        for (UserActiveInfo.ActiveInfo.ItemInfo item : mInfos) {
            UserActive active = null;
            if (item.getType().startsWith(FAVOUR)) {
                //喜欢
                if (item.getType().endsWith(FEEDS)) {
                    active = new UserActive(0, TYPE_FAVOUR,
                            username, portrait);
                    active.setHomeFeeds(new HomeFeeds(item.getId(),
                            item.getPic(), item.getTitle(),
                            item.getAuthor_portrait(), item.getAuthor_username(),
                            item.getSet_time()));
                } else if (item.getType().endsWith(VIDEO)) {
                    active = new UserActive(1, TYPE_FAVOUR,
                            username, portrait);
                    active.setTvFeeds(new TVFeeds(item.getVideo_id(),
                            item.getImg(), item.getViews(),
                            item.getWord(), item.getPhonetic_symbol(),
                            item.getSet_time()));
                } else if (item.getType().endsWith(WORD)) {
                    active = new UserActive(3, TYPE_FAVOUR,
                            username, portrait);
                    active.setWordItem(new WordItem(item.getId(), item.getWord(),
                            item.getPhonetic_symbol(), item.getSet_time(),
                            item.getPic()));
                } else if (item.getType().endsWith(DAILY_PIC)) {
                    active = new UserActive(4, TYPE_FAVOUR,
                            username, portrait);
                    active.setDailyCardItem(new DailyCardItem(item.getId(),
                            item.getSmall_pic(), item.getDaily_pic(),
                            item.getSet_time()));
                } else {

                }
            } else if (item.getType().startsWith(COMMENT)) {
                //评论
                if (item.getType().endsWith(FEEDS)) {
                    active = new UserActive(0, TYPE_COMMENT,
                            username, portrait, item.getComment());
                    active.setHomeFeeds(new HomeFeeds(item.getId(),
                            item.getPic(), item.getTitle(),
                            item.getAuthor_portrait(), item.getAuthor_username(),
                            item.getSet_time()));
                } else if (item.getType().endsWith(VIDEO)) {
                    active = new UserActive(1, TYPE_COMMENT,
                            username, portrait, item.getComment());
                    active.setTvFeeds(new TVFeeds(item.getVideo_id(),
                            item.getImg(), item.getViews(),
                            item.getWord(), item.getPhonetic_symbol(),
                            item.getSet_time()));
                } else if (item.getType().endsWith(WORD)) {
                    active = new UserActive(3, TYPE_COMMENT,
                            username, portrait, item.getComment());
                    active.setWordItem(new WordItem(item.getId(), item.getWord(),
                            item.getPhonetic_symbol(), item.getSet_time(),
                            item.getPic()));
                } else if (item.getType().endsWith(DAILY_PIC)) {
                    active = new UserActive(4, TYPE_COMMENT,
                            username, portrait, item.getComment());
                    active.setDailyCardItem(new DailyCardItem(item.getId(),
                            item.getSmall_pic(), item.getDaily_pic(),
                            item.getSet_time()));
                } else {

                }
            } else {
                //回复
            }
            if (active != null) {
                mItems.add(active);
            }
        }
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
        void onChange(String insist_day);
    }
}
