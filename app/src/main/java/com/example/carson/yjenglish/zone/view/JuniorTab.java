package com.example.carson.yjenglish.zone.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordTag;
import com.example.carson.yjenglish.zone.viewbinder.WordTagViewBinder;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JuniorTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JuniorTab extends Fragment implements WordTagViewBinder.OnTagClickListener {

    private RecyclerView recyclerView;
    private MultiTypeAdapter mAdapter;
    private Items items;

    public JuniorTab() {
        // Required empty public constructor
    }

    public static JuniorTab newInstance() {
        JuniorTab fragment = new JuniorTab();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        initRecyclerViews(view);
        return view;
    }

    private void initRecyclerViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        items = new Items();

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (items.get(position) instanceof WordTag) ? 1 : 3;
            }
        });
        recyclerView.setLayoutManager(manager);
        mAdapter = new MultiTypeAdapter(items);
        mAdapter.register(EmptyValue.class, new FieldTitleViewBinder());
        mAdapter.register(WordTag.class, new WordTagViewBinder(this));

        recyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        items.add(new EmptyValue("初中七年级起"));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTagClick(String tag) {

    }
}
