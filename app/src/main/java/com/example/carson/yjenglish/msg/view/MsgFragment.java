package com.example.carson.yjenglish.msg.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.carson.yjenglish.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MsgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MsgFragment extends Fragment {

    private LinearLayout mTabLayout;
    private TextView tab1Text, tab2Text, tab3Text;
    private ImageView tab1Img, tab2Img, tab3Img;
    private LinearLayout tab1, tab2, tab3;
    private ConstraintLayout mContainer;

    private CommentFragment mTab1;
    private LikeFragment mTab2;
    private SysNoticeFragment mTab3;

    private MsgReceiver msgReceiver;

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

        tab1Text = view.findViewById(R.id.tab_comment_text);
        tab2Text = view.findViewById(R.id.tab_like_text);
        tab3Text = view.findViewById(R.id.tab_notice_text);
        tab1Img = view.findViewById(R.id.tab_comment_img);
        tab2Img = view.findViewById(R.id.tab_like_img);
        tab3Img = view.findViewById(R.id.tab_notice_img);

        setDefaultTab();
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab3Text.setSelected(false);
                tab2Text.setSelected(false);
                tab2Img.setSelected(false);
                tab3Img.setSelected(false);
                tab1Img.setImageResource(R.drawable.selector_msg_comment);
                if (mCurrentPos != 0) {
                    tab1Text.setSelected(true);
                    tab1Img.setSelected(true);
                    tab1Text.setTextColor(Color.parseColor("#5ee1c9"));
                    tab2Text.setTextColor(Color.parseColor("#97919191"));
                    tab3Text.setTextColor(Color.parseColor("#97919191"));
                    switchTab(0);
                    mCurrentPos = 0;
                }
            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1Text.setSelected(false);
                tab1Img.setSelected(false);
                tab3Text.setSelected(false);
                tab3Img.setSelected(false);
                tab2Img.setImageResource(R.drawable.selector_msg_like);
                if (mCurrentPos != 1) {
                    tab2Text.setSelected(true);
                    tab2Img.setSelected(true);
                    tab2Text.setTextColor(Color.parseColor("#ffabbc"));
                    tab1Text.setTextColor(Color.parseColor("#97919191"));
                    tab3Text.setTextColor(Color.parseColor("#97919191"));
                    switchTab(1);
                    mCurrentPos = 1;
                }

            }
        });

        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1Text.setSelected(false);
                tab2Text.setSelected(false);
                tab1Img.setSelected(false);
                tab2Img.setSelected(false);
                tab3Img.setImageResource(R.drawable.selector_msg_notice);
                if (mCurrentPos != 2) {
                    tab3Img.setSelected(true);
                    tab3Text.setSelected(true);
                    tab3Text.setTextColor(Color.parseColor("#f2bf27"));
                    tab2Text.setTextColor(Color.parseColor("#97919191"));
                    tab1Text.setTextColor(Color.parseColor("#97919191"));
                    switchTab(2);
                    mCurrentPos = 2;
                }

            }
        });



    }

    private void setDefaultTab() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        tab1Text.setSelected(true);
        tab1Img.setSelected(true);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        tab1Text.setTextColor(Color.parseColor("#5ee1c9"));
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

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("tabPos", 0);
            boolean isRefresh = intent.getBooleanExtra("isRefresh", false);
            switch (pos) {
                case 0:
                    if (isRefresh) {
                        tab1Img.setImageResource(R.drawable.selector_msg_comment);
                    } else {
                        tab1Img.setImageResource(R.drawable.selector_msg_comment_notification);
                    }
                    break;
                case 1:
                    if (isRefresh) {
                        tab2Img.setImageResource(R.drawable.selector_msg_like);
                    } else {
                        tab2Img.setImageResource(R.drawable.selector_msg_like_notification);
                    }
                    break;
                case 2:
                    if (isRefresh) {
                        tab3Img.setImageResource(R.drawable.selector_msg_notice);
                    } else {
                        tab3Img.setImageResource(R.drawable.selector_msg_notice_notification);
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NEW_MSG_ITEM_ADD");
        msgReceiver = new MsgReceiver();
        if (getActivity() != null) {
            getActivity().registerReceiver(msgReceiver, intentFilter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (msgReceiver != null && getActivity() != null) {
            getActivity().unregisterReceiver(msgReceiver);
            msgReceiver = null;
        }
    }
}
