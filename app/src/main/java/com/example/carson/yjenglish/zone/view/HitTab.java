package com.example.carson.yjenglish.zone.view;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordTag;
import com.example.carson.yjenglish.zone.viewbinder.WordTagViewBinder;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HitTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HitTab extends Fragment implements WordTagViewBinder.OnTagClickListener {

    private RecyclerView recyclerView;
    private MultiTypeAdapter mAdapter;

    private Items items;

    public HitTab() {
        // Required empty public constructor
    }

    public static HitTab newInstance() {
        HitTab fragment = new HitTab();
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

        items = new Items();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (items.get(position) instanceof WordTag) {
                    return 1;
                } else {
                    return 3;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new MultiTypeAdapter(items);
        mAdapter.register(EmptyValue.class, new FieldTitleViewBinder());
        mAdapter.register(WordTag.class, new WordTagViewBinder(this));

        recyclerView.setAdapter(mAdapter);
        loadData();
    }

    private void loadData() {
        items.add(new EmptyValue("小学一年级起"));
        for (int i = 0; i < 10; i++) {
            items.add(new WordTag("小学人教版一年级上" + i, (i + 1) * 10 + "单词"));
        }
        /**
         * 一下几个空data为的是拉高recyclerview的底部间距 免得修改layout_recycler_view.xml 以影响其他布局
         */
        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));
        items.add(new EmptyValue(""));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTagClick(String tag) {
        DialogUtils dialogUtils = DialogUtils.getInstance(getActivity());
        final Dialog mDialog = dialogUtils.newPickerDialog(tag);
        dialogUtils.show(mDialog);
        dialogUtils.setPickerListener(new DialogUtils.OnPickerListener() {
            @Override
            public void onConfirm(String day, String count, String date) {
                Toast.makeText(getContext(), "day = " + day + " count = " + count
                 + " date = " + date, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
    }
}
