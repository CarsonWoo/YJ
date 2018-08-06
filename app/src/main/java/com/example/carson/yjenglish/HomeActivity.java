package com.example.carson.yjenglish;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.example.carson.yjenglish.customviews.BadgeView;
import com.example.carson.yjenglish.discover.DiscoverFragment;
import com.example.carson.yjenglish.home.view.HomeFragment;
import com.example.carson.yjenglish.home.view.WordListAty;
import com.example.carson.yjenglish.msg.MsgFragment;
import com.example.carson.yjenglish.tv.view.TVFragment;
import com.example.carson.yjenglish.zone.ZoneFragment;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements HomeFragment.OnHomeInteractListener {

    private final String TAG = "HomeActivity";

    private TabLayout tabLayout;
    private ConstraintLayout container;

    private FragmentManager fm;
    private HomeFragment homeFragment;
    private TVFragment tvFragment;
    private DiscoverFragment disFragment;
    private MsgFragment msgFragment;
    private ZoneFragment zoneFragment;

    //设置一个广播 监听message数据列表的更新
    private MsgBroadCastReceiver mMsgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();


    }



    private void initViews() {
        tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.home_container);

        setDefaultFragment();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setContainerView(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    private void setDefaultFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.home_container, HomeFragment.newInstance());
        ft.commit();
    }

    private void setContainerView(TabLayout.Tab tab) {
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        hideAllFragments(transaction);
        switch (tab.getPosition()) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
                    transaction.add(R.id.home_container, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                if (tvFragment == null) {
                    tvFragment = TVFragment.newInstance();
                    transaction.add(R.id.home_container, tvFragment);
                } else {
                    transaction.show(tvFragment);
                }
                break;
            case 2:
                if (disFragment == null) {
                    disFragment = DiscoverFragment.newInstance();
                    transaction.add(R.id.home_container, disFragment);
                } else {
                    transaction.show(disFragment);
                }
                break;
            case 3:
                tab.setIcon(R.drawable.selector_msg);
                if (msgFragment == null) {
                    msgFragment = MsgFragment.newInstance();
                    transaction.add(R.id.home_container, msgFragment);
                } else {
                    transaction.show(msgFragment);
                }
                break;
            case 4:
                if (zoneFragment == null) {
                    zoneFragment = ZoneFragment.newInstance();
                    transaction.add(R.id.home_container, zoneFragment);
                } else {
                    transaction.show(zoneFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideAllFragments(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (tvFragment != null) {
            ft.hide(tvFragment);
        }
        if (disFragment != null) {
            ft.hide(disFragment);
        }
        if (msgFragment != null) {
            ft.hide(msgFragment);
        }
        if (zoneFragment != null) {
            ft.hide(zoneFragment);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_home;
    }

    public class MsgBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            tabLayout.getTabAt(3).setIcon(R.drawable.ic_msg_unsel_notification);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MSG_LIST_CHANGE");
        mMsgReceiver = new MsgBroadCastReceiver();
        registerReceiver(mMsgReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMsgReceiver != null) {
            unregisterReceiver(mMsgReceiver);
            mMsgReceiver = null;
        }
    }

    /**
     * 点击音乐播放的回调
     * @param view
     */
    @Override
    public void onMusicPressed(View view) {

    }

    /**
     * 点击item的回调
     * @param item
     */
    @Override
    public void onItemClick(ArrayList item) {

    }

    @Override
    public void onProgressClick(View view) {
        Intent toWordList = new Intent(HomeActivity.this, WordListAty.class);
        startActivity(toWordList);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.e(TAG, "onAttachFragment");
        if (homeFragment == null && fragment instanceof HomeFragment) {
            homeFragment = (HomeFragment) fragment;
        } else if (tvFragment == null && fragment instanceof TVFragment) {
            tvFragment = (TVFragment) fragment;
        } else if (disFragment == null && fragment instanceof DiscoverFragment) {
            disFragment = (DiscoverFragment) fragment;
        } else if (msgFragment == null && fragment instanceof MsgFragment) {
            msgFragment = (MsgFragment) fragment;
        } else if (zoneFragment == null && fragment instanceof ZoneFragment) {
            zoneFragment = (ZoneFragment) fragment;
        }
    }
}
