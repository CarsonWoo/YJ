package com.example.carson.yjenglish.zone.view.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.PickerView;
import com.example.carson.yjenglish.utils.UserConfig;

import java.util.ArrayList;
import java.util.List;

public class RememSettingAty extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TextView confirm;
    private Switch notifySwitch;
    private LinearLayout control;
    private PickerView hourPicker, minutePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remem_setting);
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        confirm = findViewById(R.id.btn_confirm);
        control = findViewById(R.id.control);
        hourPicker = findViewById(R.id.hour_picker);
        minutePicker = findViewById(R.id.minute_picker);
        notifySwitch = findViewById(R.id.switch_notify);

        back.setOnClickListener(this);
        confirm.setOnClickListener(this);

        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours.add("0" + i + "时");
            } else {
                hours.add(i + "时");
            }
        }
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                minutes.add("0" + i + "分");
            } else {
                minutes.add(i + "分");
            }
        }

        hourPicker.setData(hours);
        minutePicker.setData(minutes);

        if (UserConfig.shouldSendNotification(this)) {
            notifySwitch.setChecked(true);
            control.setEnabled(true);
            control.setContextClickable(true);
            confirm.setVisibility(View.VISIBLE);
            hourPicker.setScrollable(true);
            minutePicker.setScrollable(true);
        } else {
            notifySwitch.setChecked(false);
            control.setEnabled(false);
            control.setContextClickable(false);
            confirm.setVisibility(View.GONE);
            hourPicker.setScrollable(false);
            minutePicker.setScrollable(false);
        }

        notifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    control.setEnabled(true);
                    confirm.setVisibility(View.VISIBLE);
                    hourPicker.setScrollable(true);
                    minutePicker.setScrollable(true);
                } else {
                    control.setEnabled(false);
                    confirm.setVisibility(View.GONE);
                    hourPicker.setScrollable(false);
                    minutePicker.setScrollable(false);
                    UserConfig.cacheShouldSendNotification(RememSettingAty.this, false);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.btn_confirm:
                doConfirmWork();
                break;
            default:
                break;
        }
    }

    private void doConfirmWork() {
        UserConfig.cacheShouldSendNotification(this, true);
    }
}
