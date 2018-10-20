package com.example.carson.yjenglish.zone.view.setting;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;

public class ManageAty extends AppCompatActivity {

    private ImageView back;

    private TextView current;
    private TextView bindFirst;
    private TextView bindSecond;

    private ImageView imgCurrent;
    private ImageView imgFirst;
    private ImageView imgSecond;

    private Button btnCurrent;
    private Button btnFirst;
    private Button btnSecond;

    private TYPE currentSelected;

    private enum TYPE {
        PHONE,
        WECHAT,
        QQ
    }

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
        setContentView(R.layout.activity_manage);

        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        current = findViewById(R.id.current_way_text);
        imgCurrent = findViewById(R.id.current_way_img);
        btnCurrent = findViewById(R.id.btn_current);

        bindFirst = findViewById(R.id.other_way_text_1);
        imgFirst = findViewById(R.id.other_way_img_1);
        btnFirst = findViewById(R.id.btn_other_1);

        bindSecond = findViewById(R.id.other_way_text_2);
        imgSecond = findViewById(R.id.other_way_img_2);
        btnSecond = findViewById(R.id.btn_other_2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bindCurrent();
        //需联网获取wechat或qq或手机号
        bindOther(currentSelected);
    }

    private void bindOther(TYPE currentSelected) {
        switch (currentSelected) {
            case PHONE:
                bindFirst.setText("微信");
                bindSecond.setText("QQ");

                imgFirst.setImageResource(R.drawable.selector_bind_wechat);
                imgSecond.setImageResource(R.drawable.selector_bind_qq);

                setBtnChange(btnFirst, true);
                setBtnChange(btnSecond, true);
                break;
            case WECHAT:
                bindFirst.setText("手机");
                bindSecond.setText("QQ");

                imgFirst.setImageResource(R.drawable.selector_bind_phone);
                imgSecond.setImageResource(R.drawable.selector_bind_qq);

                setBtnChange(btnFirst, true);
                setBtnChange(btnSecond, true);
                break;
            case QQ:
                bindFirst.setText("手机");
                bindSecond.setText("微信");

                imgFirst.setImageResource(R.drawable.selector_bind_phone);
                imgSecond.setImageResource(R.drawable.selector_bind_wechat);

                setBtnChange(btnFirst, true);
                setBtnChange(btnSecond, true);
                break;
            default:
                break;
        }
    }

    private void setBtnChange(Button btn, boolean isNull) {
        if (isNull) {
            btn.setText("绑定");
            btn.setTextColor(Color.WHITE);
            btn.setBackgroundResource(R.drawable.solid_half_circle_btn_main);
        } else {
            btn.setText("更改");
            btn.setTextColor(getResources().getColor(R.color.colorAccent));
            btn.setBackgroundResource(R.drawable.stroke_half_circle_btn_main);
        }
    }

    private void bindCurrent() {
        if (UserConfig.getPhone(this) != null && !UserConfig.getPhone(this).isEmpty()) {
            current.setText("手机：" + UserConfig.getPhone(this));
            imgCurrent.setImageResource(R.drawable.selector_bind_phone);
            imgCurrent.setSelected(true);
            currentSelected = TYPE.PHONE;
        } else if (UserConfig.getWechat(this) != null && !UserConfig.getWechat(this).isEmpty()) {
            current.setText("微信：" + UserConfig.getWechat(this));
            imgCurrent.setImageResource(R.drawable.selector_bind_wechat);
            imgCurrent.setSelected(true);
            currentSelected = TYPE.WECHAT;
        } else if (UserConfig.getQQ(this) != null && !UserConfig.getPhone(this).isEmpty()) {
            current.setText("QQ：" + UserConfig.getQQ(this));
            imgCurrent.setImageResource(R.drawable.selector_bind_qq);
            imgCurrent.setSelected(true);
            currentSelected = TYPE.QQ;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
