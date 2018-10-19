package com.example.carson.yjenglish;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
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
import com.example.carson.yjenglish.discover.view.DiscoverFragment;
import com.example.carson.yjenglish.home.HomeInfoTask;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.home.presenter.HomeInfoPresenter;
import com.example.carson.yjenglish.home.view.HomeFragment;
import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.music.view.MusicActivity;
import com.example.carson.yjenglish.home.view.word.WordListAty;
import com.example.carson.yjenglish.msg.view.MsgFragment;
import com.example.carson.yjenglish.tv.view.TVFragment;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.NotificationUtil;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.view.setting.SettingAty;
import com.example.carson.yjenglish.zone.view.ZoneFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private HomeInfoPresenter homePresenter;

//    private Map<Integer, NotifyObject> notifyObjects = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setTintColor(Color.parseColor("#5ee1c9"));

        SharedPreferences sp = getSharedPreferences("YJEnglish", MODE_PRIVATE);
        boolean disableNotify = sp.getBoolean("notification", false);//根据设置中的消息免打扰决定是否开启
        if (!disableNotify) {
            //未启动消息免打扰
            long now = System.currentTimeMillis();
            long inter = 3 * AlarmManager.INTERVAL_HOUR;
            SimpleDateFormat smf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

            Date date = new Date(now+inter);

            NotifyObject obj = new NotifyObject();

            obj.type = 1;
            obj.title = "小呗秘书";
//            obj.subText = "理论提醒时间：" + smf.format(date);
            Log.e(TAG, "理论提醒时间：" + smf.format(date));
            obj.content = "今天你背了吗~";
            obj.firstTime = now + inter;
            obj.activityClass = HomeActivity.this.getClass();
            obj.icon = R.mipmap.logo_1;

            NotificationUtil.notifySpecificByAlarm(getApplicationContext(), obj);

//            notifyObjects.put(obj.type,obj);
//            NotificationUtil.notifyByAlarm(getApplicationContext() ,this.notifyObjects);
        }


        initViews();
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.home_container);

        tabLayout.addTab(tabLayout.newTab().setText("首页").setIcon(getResources().getDrawable(R.drawable.selector_home)));
        tabLayout.addTab(tabLayout.newTab().setText("语境").setIcon(getResources().getDrawable(R.drawable.selector_tv)));
        tabLayout.addTab(tabLayout.newTab().setText("发现").setIcon(getResources().getDrawable(R.drawable.selector_discover)));
        tabLayout.addTab(tabLayout.newTab().setText("消息").setIcon(getResources().getDrawable(R.drawable.selector_msg)));
        tabLayout.addTab(tabLayout.newTab().setText("我的").setIcon(getResources().getDrawable(R.drawable.selector_zone)));

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

    //设置主界面
    private void setDefaultFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        homeFragment = HomeFragment.newInstance();
        executeHomeTask();
        ft.add(R.id.home_container, homeFragment);
        StatusBarUtil.setStatusBar(this, true, false);
        ft.commit();
    }

    private void executeHomeTask() {
        HomeInfoTask homeTask = HomeInfoTask.getInstance();
        homePresenter = new HomeInfoPresenter(homeTask, homeFragment);
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
                StatusBarUtil.setStatusBar(this, true, false);
                break;
            case 1:
                if (tvFragment == null) {
                    tvFragment = TVFragment.newInstance();
                    transaction.add(R.id.home_container, tvFragment);
                } else {
                    transaction.show(tvFragment);
                }
                StatusBarUtil.setStatusBar(this, false, true);
                break;
            case 2:
                if (disFragment == null) {
                    disFragment = DiscoverFragment.newInstance();
                    transaction.add(R.id.home_container, disFragment);
                } else {
                    transaction.show(disFragment);
                }
                StatusBarUtil.setStatusBar(this, false, true);
                break;
            case 3:
                tab.setIcon(R.drawable.selector_msg);
                if (msgFragment == null) {
                    msgFragment = MsgFragment.newInstance();
                    transaction.add(R.id.home_container, msgFragment);
                } else {
                    transaction.show(msgFragment);
                }
                StatusBarUtil.setStatusBar(this, false, true);
                break;
            case 4:
                if (zoneFragment == null) {
                    zoneFragment = ZoneFragment.newInstance();
                    transaction.add(R.id.home_container, zoneFragment);
                } else {
                    transaction.show(zoneFragment);
                }
                StatusBarUtil.setStatusBar(this, false, false);
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
                AlertDialog mDialog = dialogUtils.newCommonDialog("无网络连接", R.mipmap.ic_no_network, false);
                dialogUtils.show(mDialog);
                break;
            case 0://移动网络
            case 1://WIFI网络
                break;
        }
    }

    @Override
    public void onSetting() {
        Intent toSetting = new Intent(this, SettingAty.class);
        startActivityForResult(toSetting, 100);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    //广播收到通知有变化
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
        //获取到退出的resultCode 清除缓存并跳转到登录页面
        if (requestCode == 100 && resultCode == 1001) {
            UserConfig.clearUserInfo(this);
            SharedPreferences.Editor editor = getSharedPreferences("word_favours", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            DataSupport.deleteAll(LoadHeader.class);
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
