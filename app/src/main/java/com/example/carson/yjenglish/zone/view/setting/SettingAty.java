package com.example.carson.yjenglish.zone.view.setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.NotificationUtil;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.example.carson.yjenglish.zone.ZoneService;

import org.litepal.crud.DataSupport;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingAty extends AppCompatActivity {

    private ImageView back;
    private TextView downloadControl;
    private TextView rememberWord;
    private TextView rememberSetting;
    private Switch soundSwtich;
    private Switch lockScreenSwitch;
    private Switch autoWifiSwitch;
    private TextView privateSetting;
    private Switch publicSwitch;
    private Switch notification;
    private ImageView bindPhone;
    private ImageView bindWechat;
    private ImageView bindQQ;
    private TextView advice;
    private TextView about;
    private TextView exit;
    private TextView accountManage;
    private ImageView ivRememSetting;
    private ImageView ivPrivateSetting;

    private ConstraintLayout layoutRememberSetting;
    private ConstraintLayout layoutPrivateSetting;

    private ConstraintSet originRememSet = new ConstraintSet();
    private ConstraintSet applyRememSet = new ConstraintSet();

    private ConstraintSet originPriSet = new ConstraintSet();
    private ConstraintSet applyPriSet = new ConstraintSet();

    private boolean isRememEllipse = false;
    private boolean isPriEllipse = false;

    private FrameLayout addViewContainer;

    private SharedPreferences sp;

    private static final String TAG = "SettingAty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_setting);

        initViews();
        sp = getSharedPreferences("YJEnglish", MODE_PRIVATE);
        soundSwtich.setChecked(sp.getBoolean("sound", true));
//        lockScreenSwitch.setChecked(sp.getBoolean("lock", false));
//        autoWifiSwitch.setChecked(sp.getBoolean("wifi", false));
        publicSwitch.setChecked(sp.getBoolean("public", true));
        notification.setChecked(sp.getBoolean("notification",false));

        bindAccount();

    }

    private void bindAccount() {
        if (UserConfig.getPhone(this) != null && !UserConfig.getPhone(this).isEmpty()) {
            bindPhone.setSelected(true);
        } else {
            bindPhone.setSelected(false);
        }

        if (UserConfig.getWechat(this) != null && !UserConfig.getWechat(this).isEmpty()) {
            bindWechat.setSelected(true);
        } else {
            bindWechat.setSelected(false);
        }

        if (UserConfig.getQQ(this) != null && !UserConfig.getQQ(this).isEmpty()) {
            bindQQ.setSelected(true);
        } else {
            bindQQ.setSelected(false);
        }
    }

    private void initViews() {
        back = findViewById(R.id.back);
        downloadControl = findViewById(R.id.download_control);
        rememberWord = findViewById(R.id.notify_remember_word);
        rememberSetting = findViewById(R.id.remember_setting);
        soundSwtich = findViewById(R.id.switch_sound);
        lockScreenSwitch = findViewById(R.id.switch_lock_screen);
        autoWifiSwitch = findViewById(R.id.switch_auto_play_wifi);
        privateSetting = findViewById(R.id.private_setting);
        publicSwitch = findViewById(R.id.switch_to_public);
        notification = findViewById(R.id.switch_notification);
        bindPhone = findViewById(R.id.phone_bind);
        bindWechat = findViewById(R.id.wechat_bind);
        bindQQ = findViewById(R.id.qq_bind);
        advice = findViewById(R.id.advice);
        about = findViewById(R.id.about);
        exit = findViewById(R.id.exit);
        accountManage = findViewById(R.id.account_manage);
        addViewContainer = findViewById(R.id.add_view_container);
        ivRememSetting = findViewById(R.id.iv_remember_setting);
        ivPrivateSetting = findViewById(R.id.iv_private_setting);

        layoutRememberSetting = findViewById(R.id.layout_remember_setting);
        layoutPrivateSetting = findViewById(R.id.layout_private_setting);

        originRememSet.clone(layoutRememberSetting);
        applyRememSet.clone(layoutRememberSetting);

        originPriSet.clone(layoutPrivateSetting);
        applyPriSet.clone(layoutPrivateSetting);

        publicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                postIsOpen2Server();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doExitWork();
            }
        });

        rememberWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRemember();
            }
        });

        advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdviceWork();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAbout();
            }
        });

        accountManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAccountManage();
            }
        });

        rememberSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEllipseTask(ivRememSetting, layoutRememberSetting, applyRememSet, originRememSet,
                        isRememEllipse, R.id.notify_sound, R.id.switch_sound, R.id.notify_lock_screen,
                        R.id.switch_lock_screen, R.id.switch_auto_play_wifi, R.id.notify_auto_play_wifi);
                isRememEllipse = !isRememEllipse;
            }
        });

        privateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEllipseTask(ivPrivateSetting, layoutPrivateSetting, applyPriSet, originPriSet,
                        isPriEllipse, R.id.notify_to_public, R.id.switch_to_public, R.id.switch_notification,
                        R.id.notify_notification);
                isPriEllipse = !isPriEllipse;
            }
        });

        downloadControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDownloadManage();
            }
        });
    }

    //向服务器post是否公开动态
    private void postIsOpen2Server() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(ZoneService.class).setActiveIsOpen(UserConfig.getToken(this))
                .enqueue(new Callback<CommonInfo>() {
                    @Override
                    public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                        CommonInfo info = response.body();
                        if (info.getStatus().equals("200")) {
                            sp.edit().putBoolean("public", publicSwitch.isChecked()).apply();
                        } else {
                            Log.e(TAG, info.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonInfo> call, Throwable t) {

                    }
                });
    }

    private void toDownloadManage() {
        Intent toDownload = new Intent(this, DownloadManageAty.class);
        startActivity(toDownload);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    //将radiogroup收起
    private void doEllipseTask(ImageView img, final ConstraintLayout layout, final ConstraintSet apply,
                               final ConstraintSet origin, final boolean isEllipse, final int... ids) {
        ObjectAnimator animator;
        if (!isEllipse) {
            animator = ObjectAnimator.ofFloat(img, "rotation", 0, -180);
            animator.setDuration(500);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    setItemTransition(layout, apply, isEllipse, ids);
                }
            });
        } else {
            animator = ObjectAnimator.ofFloat(img, "rotation", -180, 0);
            animator.setDuration(500);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    setItemTransition(layout, origin, isEllipse, ids);
                }
            });
        }
        animator.start();
    }

    private void setItemTransition(ConstraintLayout layout, ConstraintSet set, boolean isEllipse, int... ids ) {
        TransitionManager.beginDelayedTransition(layout);
        if (!isEllipse) {
            set.clear(ids[0]);
            for (int i = 0; i < ids.length - 1; i++) {
                set.clear(ids[i + 1]);
                set.connect(ids[i], ConstraintSet.TOP, ids[i + 1], ConstraintSet.BOTTOM);
                set.connect(ids[i + 1], ConstraintSet.BOTTOM, ids[i], ConstraintSet.TOP);
            }

            set.createVerticalChain(ids[0], ConstraintSet.TOP, ids[ids.length - 1], ConstraintSet.BOTTOM,
                    new int[]{ids[0], ids[ids.length - 1]}, null, ConstraintSet.CHAIN_PACKED);
        }
        set.applyTo(layout);
    }

    //账号管理
    private void toAccountManage() {
        Dialog dialog = DialogUtils.getInstance(this).newCommonDialog("暂时还没开通绑定其他方式的功能噢..",
                R.mipmap.welfare_place_holder, false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 260);
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
//        Intent toManage = new Intent(this, ManageAty.class);
//        startActivity(toManage);
//        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    //关于
    private void toAbout() {
        Intent toAbout = new Intent(this, AboutAty.class);
        startActivity(toAbout);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    //意见反馈
    private void doAdviceWork() {
        Intent toAdvice = new Intent(this, AdviceAty.class);
        startActivity(toAdvice);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    //提醒背单词
    private void toRemember() {
        Intent toRemem = new Intent(this, RememSettingAty.class);
        startActivity(toRemem);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    //退出程序
    private void doExitWork() {
        setResult(1001);
        finish();
    }

    @Override
    public void onBackPressed() {
        //缓存各种状态
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("sound", soundSwtich.isChecked());
//        editor.putBoolean("lock", lockScreenSwitch.isChecked());
//        editor.putBoolean("wifi", autoWifiSwitch.isChecked());

        editor.putBoolean("notification", notification.isChecked());
        editor.apply();
        if (notification.isChecked()) {
            NotificationUtil.clearSpecificNotification(getApplicationContext(), 1);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
