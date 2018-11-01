package com.example.carson.yjenglish;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.example.carson.yjenglish.broadcastreceiver.NetworkReceiver;
import com.example.carson.yjenglish.discover.view.DiscoverFragment;
import com.example.carson.yjenglish.home.HomeInfoTask;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.home.WordInfoTask;
import com.example.carson.yjenglish.home.WordService;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.home.model.word.WordInfo;
import com.example.carson.yjenglish.home.presenter.HomeInfoPresenter;
import com.example.carson.yjenglish.home.view.HomeFragment;
import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.music.view.MusicActivity;
import com.example.carson.yjenglish.home.view.word.WordListAty;
import com.example.carson.yjenglish.msg.view.MsgFragment;
import com.example.carson.yjenglish.tv.view.TVFragment;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.DownloadUtils;
import com.example.carson.yjenglish.utils.FileUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.NotificationUtil;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.view.setting.SettingAty;
import com.example.carson.yjenglish.zone.view.ZoneFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tencent.bugly.beta.Beta;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity implements HomeFragment.OnHomeInteractListener,
        NetUtils.NetEvent, ZoneFragment.OnZoneEventListener, DownloadListener {

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

    private String basePath = Environment.getExternalStorageDirectory().getPath() + "/背呗背单词/";

    private List<WordInfo.ListObject.WordData> totalList = new ArrayList<>();

    private HomeWordDownloadReceiver downloadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        Beta.checkUpgrade(false,false);

//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setTintColor(Color.parseColor("#5ee1c9"));

        if (UserConfig.getSelectedPlan(this) != null && !UserConfig.getSelectedPlan(this).isEmpty()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                File dir = new File(basePath + UserConfig.getSelectedPlan(this) + "/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                executeDownloadTask(dir.getPath());
            }
        }

        if (UserConfig.shouldSendNotification(this)) {
            notifyService();
        }

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

    private void executeDownloadTask(String path) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(WordService.class).startLearning(UserConfig.getToken(this))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WordInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WordInfo wordInfo) {
                        if (wordInfo.getStatus().equals("200")) {
                            totalList.clear();
                            totalList.addAll(wordInfo.getData().getOld_list());
                            totalList.addAll(wordInfo.getData().getNew_list());
//                            DownloadUtils downloadUtils = new DownloadUtils("httP://47.107.62.22/", HomeActivity.this);
                            for (WordInfo.ListObject.WordData data : totalList) {
                                File file;
                                if (data.getPic().endsWith("jpg")) {
                                    file = new File(path + "/" + data.getWord() + ".jpg");
                                } else {
                                    file = new File(path + "/" + data.getWord() + ".png");
                                }
                                if (!file.exists()) {
                                    //不存在则下载
                                    doDownload(file, data.getPic());
//                                    String url = data.getPic().substring("http://47.107.62.22/".length());
//                                    downloadUtils.download(url, file.getPath(), new Subscriber() {
//                                        @Override
//                                        public void onCompleted() {
//
//                                        }
//
//                                        @Override
//                                        public void onError(Throwable e) {
//
//                                        }
//
//                                        @Override
//                                        public void onNext(Object o) {
//
//                                        }
//                                    });
                                }

                            }
                        }
                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    private void doDownload(File file, String url) {
        new AsyncTask<Void, Integer, File>() {

            @Override
            protected File doInBackground(Void... voids) {
                File tmpFile = null;

                try {
                    FutureTarget<File> future = Glide.with(HomeActivity.this)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                    tmpFile = future.get();

                    FileInputStream fis = new FileInputStream(tmpFile);

                    FileOutputStream fos = new FileOutputStream(file);

                    int len = 0;

                    byte[] b = new byte[1024];

                    while ((len = fis.read(b)) != -1) {
                        fos.write(b, 0, len);
                    }

                    fos.close();

                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return tmpFile;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }
        }.execute();
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
                if (tab.getPosition() == 0) {
                    if (homeFragment != null) {
                        homeFragment.onRefreshHomeItem();
                    }
                } else if (tab.getPosition() == 1) {
                    if (tvFragment != null) {
                        tvFragment.executeLoadTask();
                    }
                } else if (tab.getPosition() == 2) {
                    if (disFragment != null) {
                        disFragment.executeLoadingTask();
                    }
                } else if (tab.getPosition() == 4) {
                    if (zoneFragment != null) {
                        zoneFragment.executeTask();
                    }
                }
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
        Log.e(TAG, "executeTask()");
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
                Log.e(TAG, "onClick");
//                executeHomeTask();
                transaction.show(homeFragment);
//                homeFragment.onRefreshHomeItem();
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

    @Override
    public void onStartDownload() {

    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onFinishDownload() {
        Log.e(TAG, "download msg: download finish");
    }

    @Override
    public void onFail(String errorInfo) {
        Log.e(TAG, "download msg: " + errorInfo);
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

        downloadReceiver = new HomeWordDownloadReceiver();
        IntentFilter downloadFilter = new IntentFilter();
        downloadFilter.addAction("WORDS_START_DOWNLOAD");
        registerReceiver(downloadReceiver, downloadFilter);
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
        if (downloadReceiver != null) {
            unregisterReceiver(downloadReceiver);
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

    /**
     * 开启通知服务
     */
    private void notifyService() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String split[] = UserConfig.getNotificationTime(this).split(";");
        String hourStr = split[0].replace("时", "");
        String minStr = split[1].replace("分", "");
        int hour, minute;
        if (hourStr.startsWith("0")) {
            hour = Integer.parseInt(hourStr.substring(1));
        } else {
            hour = Integer.parseInt(hourStr);
        }
        if (minStr.startsWith("0")) {
            minute = Integer.parseInt(minStr.substring(1));
        } else {
            minute = Integer.parseInt(minStr);
        }
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //选择当前时间
        long selectTime = calendar.getTimeInMillis();

        long sysTime = System.currentTimeMillis();

        if (sysTime > selectTime) {
            //已超过 则从明天开始
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }

//        long firstTime = SystemClock.elapsedRealtime(); //开机之后到现在的运行时间
//        long time = selectTime - sysTime;
//        firstTime += time;

        NotifyObject obj = new NotifyObject();
        obj.title = "小呗秘书";
        obj.subText = "背呗学习提醒";
        obj.content = "你的背单词时间到啦，每天小小坚持，收获大大进步";
        obj.type = UserConfig.ALARM_START_STUDY_ID;
        obj.icon = R.mipmap.logo_1;
        obj.activityClass = HomeActivity.class;
        obj.firstTime = selectTime;

        Map<Integer, NotifyObject> map = new HashMap<>();
        map.put(obj.type, obj);

        NotificationUtil.notifyByAlarm(getApplicationContext(), map);
    }

    public class HomeWordDownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                File dir = new File(basePath + UserConfig.getSelectedPlan(HomeActivity.this) + "/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                executeDownloadTask(dir.getPath());
            }
        }
    }

}
