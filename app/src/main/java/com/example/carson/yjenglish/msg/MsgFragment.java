package com.example.carson.yjenglish.msg;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.msg.view.CommentFragment;
import com.example.carson.yjenglish.msg.view.LikeFragment;
import com.example.carson.yjenglish.msg.view.SysNoticeFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MsgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MsgFragment extends Fragment {

    private LinearLayout mTabLayout;
    private TextView tab1, tab2, tab3;
    private ConstraintLayout mContainer;

    private CommentFragment mTab1;
    private LikeFragment mTab2;
    private SysNoticeFragment mTab3;

    private int mCurrentPos = 0;

    public MsgFragment() {
        // Required empty public constructor
    }

    public static MsgFragment newInstance() {
        MsgFragment fragment = new MsgFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mTabLayout = view.findViewById(R.id.tab_layout);
        mContainer = view.findViewById(R.id.msg_container);
        tab1 = view.findViewById(R.id.tab_item_1);
        tab2 = view.findViewById(R.id.tab_item_2);
        tab3 = view.findViewById(R.id.tab_item_3);
        setDefaultTab();
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab3.setSelected(false);
                tab2.setSelected(false);
                if (mCurrentPos != 0) {
                    Log.e("Msg", "" + mCurrentPos);
                    tab1.setSelected(true);
                    tab1.setTextColor(Color.parseColor("#5ee1c9"));
                    tab2.setTextColor(Color.parseColor("#97919191"));
                    tab3.setTextColor(Color.parseColor("#97919191"));
                    switchTab(0);
                    mCurrentPos = 0;
                }
            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setSelected(false);
                tab3.setSelected(false);
                if (mCurrentPos != 1) {
                    Log.e("Msg", "" + mCurrentPos);
                    tab2.setSelected(true);
                    tab2.setTextColor(Color.parseColor("#ffabbc"));
                    tab1.setTextColor(Color.parseColor("#97919191"));
                    tab3.setTextColor(Color.parseColor("#97919191"));
                    switchTab(1);
                    mCurrentPos = 1;
                }

            }
        });

        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setSelected(false);
                tab2.setSelected(false);
                if (mCurrentPos != 2) {
                    Log.e("Msg", "" + mCurrentPos);
                    tab3.setSelected(true);
                    tab3.setTextColor(Color.parseColor("#f2bf27"));
                    tab2.setTextColor(Color.parseColor("#97919191"));
                    tab1.setTextColor(Color.parseColor("#97919191"));
                    switchTab(2);
                    mCurrentPos = 2;
                }

            }
        });



    }

    private void setDefaultTab() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        tab1.setSelected(true);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        tab1.setTextColor(Color.parseColor("#5ee1c9"));
        mTab1 = CommentFragment.getInstance();
        ft.add(R.id.msg_container, mTab1);
        ft.commit();
    }

    private void switchTab(int pos) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        hideAllFragments(ft);
        switch (pos) {
            case 0:
                ft.show(mTab1);
                break;
            case 1:
                if (mTab2 == null) {
                    mTab2 = LikeFragment.getInstance();
                    ft.add(R.id.msg_container, mTab2);
                } else {
                    ft.show(mTab2);
                }
                break;
            case 2:
                if (mTab3 == null) {
                    mTab3 = SysNoticeFragment.getInstance();
                    ft.add(R.id.msg_container, mTab3);
                } else {
                    ft.show(mTab3);
                }
                break;
            default:
                break;
        }
        ft.commit();
    }

    private void hideAllFragments(FragmentTransaction ft) {
        if (mTab1 != null) {
            ft.hide(mTab1);
        }
        if (mTab2 != null) {
            ft.hide(mTab2);
        }
        if (mTab3 != null) {
            ft.hide(mTab3);
        }
    }

}
