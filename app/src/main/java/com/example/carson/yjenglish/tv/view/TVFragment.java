package com.example.carson.yjenglish.tv.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.TVListAdapter;
import com.example.carson.yjenglish.tv.model.TVHeader;
import com.example.carson.yjenglish.tv.model.TVItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TVFragment extends Fragment {

    private RecyclerView rv;

    private List<TVHeader> mHeaderList;
    private List<TVItem> mItemList;

    private TVListAdapter adapter;

    public TVFragment() {
        // Required empty public constructor
    }

    public static TVFragment newInstance() {
        TVFragment fragment = new TVFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rv = view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mHeaderList = new ArrayList<>();
        mItemList = new ArrayList<>();

        TVHeader header = new TVHeader();
        header.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg");
        header.setPlayNum("1.3W");
        header.setWord("travel");
        mHeaderList.add(header);
        TVHeader header1 = new TVHeader();
        header1.setWord("color");
        header1.setPlayNum("999+");
        header1.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533311106938&di=a428eb3a3220df77190f2b9b2abef542&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20150907%2Fmp30906533_1441629699374_2.jpeg");
        mHeaderList.add(header1);

        TVItem tvItem = new TVItem();
        tvItem.setWord("travel");
        tvItem.setSoundMark("[ˈtrævəl]");
        for (int i = 0; i < 4; i ++) {
            mItemList.add(tvItem);
        }

        adapter = new TVListAdapter(getContext());
        adapter.setmHeaderList(mHeaderList);
        adapter.setmItemList(mItemList);

        rv.setAdapter(adapter);


    }

}
