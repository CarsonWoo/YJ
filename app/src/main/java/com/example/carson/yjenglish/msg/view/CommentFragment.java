package com.example.carson.yjenglish.msg.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    private static final String TAG = "CommentFragment";
    private static CommentFragment INSTANCE = null;

    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommentFragment();
        }
        return INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

}
