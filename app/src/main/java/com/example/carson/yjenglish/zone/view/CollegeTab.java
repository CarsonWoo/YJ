package com.example.carson.yjenglish.zone.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollegeTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollegeTab extends Fragment {

    private RecyclerView recyclerView;

    public CollegeTab() {
        // Required empty public constructor
    }

    public static CollegeTab newInstance() {
        CollegeTab fragment = new CollegeTab();
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
    }

}
