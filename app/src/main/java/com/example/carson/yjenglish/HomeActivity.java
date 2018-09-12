package com.example.carson.yjenglish;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.carson.yjenglish.broadcastreceiver.NetworkReceiver;
import com.example.carson.yjenglish.discover.DiscoverFragment;
import com.example.carson.yjenglish.home.HomeInfoTask;
import com.example.carson.yjenglish.home.model.HomeInfo;
import com.example.carson.yjenglish.home.model.HomeItem;
import com.example.carson.yjenglish.home.presenter.HomeInfoPresenter;
import com.example.carson.yjenglish.home.view.HomeFragment;
import com.example.carson.yjenglish.home.view.feeds.HomeItemAty;
import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.music.view.MusicActivity;
import com.example.carson.yjenglish.home.view.word.WordListAty;
import com.example.carson.yjenglish.msg.MsgFragment;
import com.example.carson.yjenglish.tv.view.TVFragment;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.view.setting.SettingAty;
import com.example.carson.yjenglish.zone.view.ZoneFragment;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements HomeFragment.OnHomeInteractListener,
        NetUtils.NetEvent, ZoneFragment.OnZoneEventListener {

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

    //设置广播 监听网络数据变化
    private NetworkReceiver mNetworkReceiver;

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
        homeFragment = HomeFragment.newInstance();
        executeHomeTask();
        ft.add(R.id.home_container, homeFragment);
        ft.commit();
    }

    private void executeHomeTask() {
        HomeInfoTask homeTask = HomeInfoTask.getInstance();
        HomeInfoPresenter homePresenter = new HomeInfoPresenter(homeTask, homeFragment);
        homeFragment.setPresenter(homePresenter);
    }

    private void setContainerView(TabLayout.Tab tab) {
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        hideAllFragments(transaction);
        switch (tab.getPosition()) {
            case 0:
                transaction.show(homeFragment);
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

    @Override
    public void onNetworkChange(int netMobile) {
        switch (netMobile) {
            case -1://无网络
                DialogUtils dialogUtils = DialogUtils.getInstance(HomeActivity.this);
                AlertDialog mDialog = dialogUtils.newCommonDialog("无网络连接", R.mipmap.ic_no_network);
                dialogUtils.show(mDialog);
                break;
            case 0://移动网络
                break;
            case 1://WIFI网络
//                executeHomeTask();
//                FragmentTransaction transaction = fm.beginTransaction();
//                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                if (tabLayout.getSelectedTabPosition() == 0) {
//                    transaction.remove(homeFragment);
//                    homeFragment = HomeFragment.newInstance();
//                    executeHomeTask();
//                    transaction.add(R.id.home_container, homeFragment);
//                    transaction.commit();
//                }
                break;
        }
    }

    @Override
    public void onSetting() {
        Intent toSetting = new Intent(this, SettingAty.class);
        startActivityForResult(toSetting, 100);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    public class MsgBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            tabLayout.getTabAt(3).setIcon(R.drawable.selector_msg_notification);
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
        Intent toMusic = new Intent(this, MusicActivity.class);
        startActivity(toMusic);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    /**
     * 点击item的回调
     * @param item
     * @param requestComment 是否一进入就跳转到评论区
     */
    @Override
    public void onItemClick(ArrayList item, boolean requestComment) {
        HomeInfo.Data.Feed mItem = (HomeInfo.Data.Feed) item.get(0);
        Intent toDetail = new Intent(this, HomeItemAty.class);
        toDetail.putExtra("video_url", mItem.getVideo());
        toDetail.putExtra("img_url", mItem.getPic());
        toDetail.putExtra("username", mItem.getAuthor_username());
        toDetail.putExtra("title", mItem.getTitle());
        toDetail.putExtra("portrait_url", mItem.getAuthor_portrait());
        toDetail.putExtra("like_num", mItem.getLikes());
        toDetail.putExtra("request_comment", requestComment);
        toDetail.putExtra("is_like", mItem.getIsLike().equals("1"));
        startActivity(toDetail);
    }

    @Override
    public void onProgressClick(View view) {
        Intent toWordList = new Intent(HomeActivity.this, WordListAty.class);
        startActivity(toWordList);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册广播
        if (mNetworkReceiver == null) {
            mNetworkReceiver = new NetworkReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetworkReceiver, filter);
            mNetworkReceiver.setNetEvent(this);
        }
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
        if (mNetworkReceiver != null) {
            unregisterReceiver(mNetworkReceiver);
            mNetworkReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(UserConfig.getToken(this))) {
            UserConfig.cacheLastDate(this);
        }
        Log.e(TAG, "onDestroy");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1001) {
            Intent toLogin = new Intent(this, LoginActivity.class);
            startActivity(toLogin);
            overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
            Log.e(TAG, "onFinish");
            finishAfterTransition();
            if (MusicActivity.INSTANCE != null) {
                Log.e(TAG, "finish");
                MusicActivity.INSTANCE.finishAffinity();
            }
        }
    }
}
